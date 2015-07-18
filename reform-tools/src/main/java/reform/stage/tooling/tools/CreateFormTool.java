package reform.stage.tooling.tools;

import reform.core.forms.Form;
import reform.core.forms.relations.FreeDirection;
import reform.core.forms.relations.ProportionalDirection;
import reform.core.forms.relations.RelativeDynamicSizeDestination;
import reform.core.forms.relations.RelativeFixSizeDestination;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.procedure.instructions.single.CreateFormInstruction;
import reform.core.runtime.relations.Direction;
import reform.core.runtime.relations.InitialDestination;
import reform.core.runtime.relations.InitialDestination.Alignment;
import reform.core.runtime.relations.ReferencePoint;
import reform.evented.core.EventedProcedure;
import reform.math.Vec2;
import reform.math.Vector;
import reform.stage.elements.SnapPoint;
import reform.stage.hittest.HitTester;
import reform.stage.hittest.HitTester.EntityFilter;
import reform.stage.tooling.Input;
import reform.stage.tooling.InstructionFocus;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;
import reform.stage.tooling.factory.FormFactory;

public class CreateFormTool implements Tool {

	private enum State {
		Idle, Snapped, Pressed, PressedSnapped

	}

	private final SelectionTool _selectionTool;
	private final FormFactory<? extends Form> _formFactory;
	private final ToolState _toolState;
	private final Cursor _cursor;
	private final HitTester _hitTester;
	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	private boolean _autoCenter = false;
	private boolean _diagonalDirection = false;
	private boolean _swapDirection = false;
	private State _state = State.Idle;
	private final Vec2 _currentOffset = new Vec2();
	private SnapPoint _startPoint;
	private SnapPoint _currentPoint;
	private CreateFormInstruction _currentInstruction;
	private InitialDestination _currentDestination;
	private ReferencePoint _currentStart;

    public CreateFormTool(final SelectionTool selectionTool,
			final FormFactory<? extends Form> formFactory,
			final ToolState toolState, final Cursor cursor,
			final HitTester hitTester, final InstructionFocus focus,
			final EventedProcedure eProcedure) {
		_selectionTool = selectionTool;
		_formFactory = formFactory;
		_toolState = toolState;
		_cursor = cursor;
		_hitTester = hitTester;
		_focus = focus;
		_eProcedure = eProcedure;
	}

	@Override
	public void setUp() {
        _toolState.setState(ToolState.State.Create);

        _toolState.setState(ToolState.State.Create);
		_toolState.setSnapPoints(
				_hitTester.getAllSnapPoints(HitTester.EntityFilter.Any));
	}

	@Override
	public void tearDown() {
		_toolState.clearSnapPoints();
	}

	@Override
	public void cancel() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			_eProcedure.removeInstruction(_currentInstruction);
			_currentInstruction = null;

            _toolState.setState(ToolState.State.Create);
			_toolState.setSnapPoints(
					_hitTester.getAllSnapPoints(HitTester.EntityFilter.Any));
			_toolState.setActiveSnapPoint(null);
			_toolState.clearEntityPoints();
			_swapDirection = false;
			_state = State.Idle;
		} else {
			_selectionTool.cancel();
		}
	}

	@Override
	public void press() {
		if (_state == State.Snapped) {
			_state = State.Pressed;
			_startPoint = _currentPoint;
			_currentStart = _startPoint.createReference();
			_currentDestination = new RelativeFixSizeDestination(_currentStart,
					new Vec2());
            final Form currentForm = _formFactory.build();
			_currentInstruction = new CreateFormInstruction(currentForm,
					_currentDestination);
			_eProcedure.addInstruction(_currentInstruction, Position.After,
					_focus.getFocused());
			_currentOffset.set(_cursor.getPosition().x - _startPoint.getX(),
                    _cursor.getPosition().y - _startPoint.getY());
			_focus.setFocus(_currentInstruction);
            _toolState.setState(ToolState.State.Create);
			_toolState.setEntityPoints(
					_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
		} else {
			_selectionTool.press();
		}
	}

	@Override
	public void release() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			if (_currentDestination.isDegenerated()) {
				cancel();
			} else {
				_state = State.Idle;
                _toolState.setState(ToolState.State.Create);
				_toolState.setSnapPoints(_hitTester
						.getAllSnapPoints(HitTester.EntityFilter.Any));
				_currentInstruction = null;
				_toolState.setActiveSnapPoint(null);
				_toolState.clearEntityPoints();
			}
		} else {
			_selectionTool.release();
            _toolState.setState(ToolState.State.Create);
		}

		_swapDirection = false;
	}

	@Override
	public void refresh() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			_toolState.setSnapPoints(_hitTester
					.getAllSnapPoints(HitTester.EntityFilter.ExcludeSelected));
		} else {
			_toolState.setSnapPoints(
					_hitTester.getAllSnapPoints(HitTester.EntityFilter.Any));
		}

		_selectionTool.refresh();
	}

	@Override
	public void toggleOption() {
		_swapDirection = !_swapDirection;
	}

	@Override
	public void cycle() {
		_cursor.cycleNextSnap();
		_selectionTool.cycle();
	}

	@Override
	public void input(final Input input) {
		switch (_state) {
		case Idle:
		case Snapped: {
			_currentPoint = _cursor.getSnapPoint(HitTester.EntityFilter.Any);
			if (_currentPoint != null) {
				_state = State.Snapped;
			} else {
				_state = State.Idle;
			}
			break;
		}
		case Pressed:
		case PressedSnapped: {
			_currentPoint = _cursor
					.getSnapPoint(HitTester.EntityFilter.ExcludeSelected);

			if (_currentPoint == null) {
				final RelativeFixSizeDestination d;
				final Vec2 delta = new Vec2(
						_cursor.getPosition().x - _startPoint.getX()
								- _currentOffset.x,
						_cursor.getPosition().y - _startPoint.getY()
								- _currentOffset.y);
				if (input.getShiftModifier().isActive()) {
					adjustVector(delta);
				}
				if (_currentDestination instanceof RelativeFixSizeDestination) {
					d = (RelativeFixSizeDestination) _currentDestination;
					d.setDelta(delta);
				} else {
					d = new RelativeFixSizeDestination(_currentStart, delta);
				}
				_state = State.Pressed;
				_currentDestination = d;
			} else {
				final RelativeDynamicSizeDestination d;
				if (_currentDestination instanceof RelativeDynamicSizeDestination) {
					d = (RelativeDynamicSizeDestination) _currentDestination;
					d.setReferenceB(_currentPoint.createReference());
				} else {
					d = new RelativeDynamicSizeDestination(_currentStart,
							_currentPoint.createReference());
				}
				if (input.getShiftModifier().isActive()) {
					d.setDirection(getDirectionFor(
							_currentPoint.getX() - _startPoint.getX(),
							_currentPoint.getY() - _startPoint.getY()));
				} else {
					d.setDirection(FreeDirection.Free);
				}
				_state = State.PressedSnapped;
				_currentDestination = d;
			}
			_currentDestination.setAlignment(
					input.getAltModifier().isActive() != _autoCenter
							? Alignment.Center : Alignment.Leading);
			_currentInstruction.setDestination(_currentDestination);
			_eProcedure.publishInstructionChange(_currentInstruction);
			break;
		}
		}

		_selectionTool.input(input);
		_toolState.setActiveSnapPoint(_currentPoint);
	}

	private Direction getDirectionFor(final double x, final double y) {
		if (_diagonalDirection) {
			return new ProportionalDirection(1);
		}

		final double absX = x < 0 ? -x : x;
		final double absY = y < 0 ? -y : y;

		final double rel = absX == 0 ? absY : absY / absX;

		if (rel > 2 != _swapDirection) {
			return Direction.CartesianDirection.Vertical;
		} else {
			return Direction.CartesianDirection.Horizontal;
		}
	}

	private void adjustVector(final Vec2 vector) {
		if (_diagonalDirection) {
			final double min = Math.min(Math.abs(vector.x), Math.abs(vector.y));
			vector.x = Math.signum(vector.x) * min;
			vector.y = Math.signum(vector.y) * min;
		} else {
			final double oldX = vector.x;
			final double oldY = vector.y;
			final double absX = oldX < 0 ? -oldX : oldX;
			final double absY = oldY < 0 ? -oldY : oldY;

			final double rel = absX == 0 ? absY : absY / absX;

			if (rel > 4) {
				vector.x = 0;
			} else if (rel < 0.25) {
				vector.y = 0;
			} else {
				final double signX = Math.signum(vector.x);
				final double signY = Math.signum(vector.y);
				vector.x = Vector.projectionX(oldX, oldY, signX, signY);
				vector.y = Vector.projectionY(oldX, oldY, signX, signY);
			}
		}
	}

	public void setAutoCenter(final boolean autocenter) {
		_autoCenter = autocenter;
	}

	public void setDiagonalDirection(final boolean diagonal) {
		_diagonalDirection = diagonal;
	}
}
