package reform.stage.tooling.tools;

import reform.core.forms.relations.ConstantDistance;
import reform.core.forms.relations.FreeDirection;
import reform.core.forms.relations.RelativeDistance;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.procedure.instructions.single.TranslateInstruction;
import reform.core.runtime.relations.Direction;
import reform.core.runtime.relations.TranslationDistance;
import reform.evented.core.EventedProcedure;
import reform.math.Vec2;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.SnapPoint;
import reform.stage.hittest.HitTester;
import reform.stage.hittest.HitTester.EntityFilter;
import reform.stage.tooling.Input;
import reform.stage.tooling.InstructionFocus;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

public class MoveFormTool implements Tool {

	private static enum State {
		Idle, Snapped, Pressed, PressedSnapped
	}

	private final SelectionTool _selectionTool;
	private final ToolState _toolState;
	private final Cursor _cursor;
	private final HitTester _hitTester;
	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	private State _state = State.Idle;
	private boolean _swapDirection;
	private final Vec2 _currentOffset = new Vec2();
	private final Vec2 _startPosition = new Vec2();
	private EntityPoint _currentPoint;
	private SnapPoint _currentSnapPoint;
	private TranslateInstruction _currentInstruction;
	private TranslationDistance _currentDistance;

	public MoveFormTool(final SelectionTool selectionTool,
			final ToolState toolState, final Cursor cursor,
			final HitTester hitTester, final InstructionFocus focus,
			final EventedProcedure eProcedure) {
		_selectionTool = selectionTool;
		_toolState = toolState;
		_cursor = cursor;
		_hitTester = hitTester;
		_focus = focus;
		_eProcedure = eProcedure;
	}

	@Override
	public void setUp() {
		_toolState.setSelectionVisible(true);
		_toolState.setEntityPoints(
				_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
	}

	@Override
	public void tearDown() {
		_toolState.clearEntityPoints();
	}

	@Override
	public void cancle() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			_eProcedure.removeInstruction(_currentInstruction);
			_currentInstruction = null;
			_toolState.clearSnapPoints();
			_toolState.setActiveEntityPoint(null);
			_toolState.setActiveSnapPoint(null);
			_state = State.Idle;
			_swapDirection = false;
		} else {
			_selectionTool.cancle();
			_toolState.clearEntityPoints();
		}
	}

	@Override
	public void press() {
		if (_state == State.Snapped) {
			_state = State.Pressed;
			_startPosition.set(_currentPoint.getX(), _currentPoint.getY());
			_currentDistance = new ConstantDistance(new Vec2());
			_currentInstruction = new TranslateInstruction(
					_currentPoint.getFormId(), _currentDistance);
			_eProcedure.addInstruction(_currentInstruction, Position.After,
					_focus.getFocused());
			_currentOffset.set(_cursor.getPosition().x - _currentPoint.getX(),
					_cursor.getPosition().y - _currentPoint.getY());
			_focus.setFocus(_currentInstruction);
		} else {
			_selectionTool.press();
			_toolState.setEntityPoints(
					_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
		}
	}

	@Override
	public void release() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			if (_currentDistance.isDegenerated()) {
				cancle();
			} else {
				_state = State.Idle;

				_toolState.clearSnapPoints();
				_currentInstruction = null;
				_toolState.setActiveSnapPoint(null);
			}
		} else {
			_selectionTool.release();
		}
		_swapDirection = false;
	}

	@Override
	public void refresh() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			_toolState.setSnapPoints(_hitTester
					.getAllSnapPoints(HitTester.EntityFilter.ExcludeSelected));
		} else {
			_toolState.clearSnapPoints();
		}

		_selectionTool.refresh();
		_toolState.setEntityPoints(
				_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
	}

	@Override
	public void toggleOption() {
		_swapDirection = !_swapDirection;
	}

	@Override
	public void cycle() {
		if (_state == State.Snapped) {
			_cursor.cycleNextEntityPoint();
		} else {
			_cursor.cycleNextSnap();
		}
		_selectionTool.cycle();

		_toolState.setEntityPoints(
				_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
	}

	@Override
	public void input(final Input input) {
		switch (_state) {
		case Idle:
		case Snapped: {
			_currentPoint = _cursor
					.getEntityPoint(HitTester.EntityFilter.OnlySelected);
			if (_currentPoint == null) {
				_state = State.Idle;
			} else {
				_state = State.Snapped;
			}

			_toolState.setActiveEntityPoint(_currentPoint);
			break;
		}
		case Pressed:
		case PressedSnapped: {
			_currentSnapPoint = _cursor
					.getSnapPoint(HitTester.EntityFilter.ExcludeSelected);

			if (_currentSnapPoint == null) {
				_state = State.Pressed;
				final Vec2 delta = new Vec2(
						_cursor.getPosition().x - _startPosition.x
								- _currentOffset.x,
						_cursor.getPosition().y - _startPosition.y
								- _currentOffset.y);
				if (input.getShiftModifier().isActive()) {
					adjustVector(delta);
				}
				if (_currentDistance instanceof ConstantDistance) {
					final ConstantDistance d = (ConstantDistance) _currentDistance;
					d.setDelta(delta);
				} else {
					_currentDistance = new ConstantDistance(delta);
				}
				_toolState.setActiveSnapPoint(null);

			} else {
				_state = State.PressedSnapped;
				final RelativeDistance d;
				if (_currentDistance instanceof RelativeDistance) {
					d = (RelativeDistance) _currentDistance;
					d.setReferenceB(_currentSnapPoint.createReference());
				} else {
					d = new RelativeDistance(_currentPoint.createReference(),
							_currentSnapPoint.createReference());
					;
				}
				if (input.getShiftModifier().isActive()) {
					d.setDirection(getDirectionFor(
							_currentSnapPoint.getX() - _startPosition.x,
							_currentSnapPoint.getY() - _startPosition.y));
				} else {
					d.setDirection(FreeDirection.Free);

				}
				_currentDistance = d;
				_toolState.setActiveSnapPoint(_currentSnapPoint);
			}

			_currentInstruction.setDistance(_currentDistance);
			_eProcedure.publishInstructionChange(_currentInstruction);
			break;
		}
		}

		_selectionTool.input(input);

		_toolState.setEntityPoints(
				_hitTester.getAllEntityPoints(EntityFilter.OnlySelected));
	}

	private Direction getDirectionFor(final double x, final double y) {
		final double absX = x < 0 ? -x : x;
		final double absY = y < 0 ? -y : y;

		final double rel = absX == 0 ? absY : absY / absX;

		if (rel > 1 != _swapDirection) {
			return Direction.CartesianDirection.Vertical;
		} else {
			return Direction.CartesianDirection.Horizontal;
		}
	}

	private void adjustVector(final Vec2 vector) {

		final double oldX = vector.x;
		final double oldY = vector.y;
		final double absX = oldX < 0 ? -oldX : oldX;
		final double absY = oldY < 0 ? -oldY : oldY;

		final double rel = absX == 0 ? absY : absY / absX;

		if (rel > 1) {
			vector.x = 0;
		} else {
			vector.y = 0;
		}
	}

}
