package reform.stage.tooling.tools;

import reform.core.forms.relations.ConstantDistance;
import reform.core.forms.relations.FreeDirection;
import reform.core.forms.relations.RelativeDistance;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.procedure.instructions.single.MorphInstruction;
import reform.core.runtime.relations.Direction;
import reform.core.runtime.relations.TranslationDistance;
import reform.evented.core.EventedProcedure;
import reform.math.Vec2;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.hittest.HitTester;
import reform.stage.hittest.HitTester.EntityFilter;
import reform.stage.hittest.HitTester.HandleFilter;
import reform.stage.tooling.Input;
import reform.stage.tooling.InstructionFocus;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

public class MorphFormTool implements Tool {

	private enum State {
		Idle, Snapped, Pressed, PressedSnapped
	}

	private final SelectionTool _selectionTool;
	private final ToolState _toolState;
	private final Cursor _cursor;
	private final HitTester _hitTester;
	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	private State _state = State.Idle;
	private final Vec2 _currentOffset = new Vec2();
	private final Vec2 _startPosition = new Vec2();
	private Handle _currentHandle;
    private MorphInstruction _currentInstruction;
	private TranslationDistance _currentDistance;
	private Instruction _baseInstruction;

	public MorphFormTool(final SelectionTool selectionTool,
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
		refreshHandles();
	}

	private void refreshHandles() {
		_toolState.setHandles(_hitTester.getAllHandles(
				EntityFilter.OnlySelected, HandleFilter.Any));
	}

	@Override
	public void tearDown() {
		_toolState.clearHandles();
	}

	@Override
	public void cancle() {
		if (_state == State.Pressed || _state == State.PressedSnapped) {
			_eProcedure.removeInstruction(_currentInstruction);
			_currentInstruction = null;
			_toolState.setActiveHandle(null);
			_toolState.setActiveSnapPoint(null);
			_toolState.clearSnapPoints();
			_baseInstruction = null;
			_state = State.Idle;
		} else {
			_selectionTool.cancle();
			_toolState.clearHandles();
		}
	}

	@Override
	public void press() {
		if (_state == State.Snapped) {
			_state = State.Pressed;
			_startPosition.set(_currentHandle.getX(), _currentHandle.getY());
			_baseInstruction = _focus.getFocused();
			_currentDistance = new ConstantDistance(new Vec2());
			_currentInstruction = new MorphInstruction(
					_currentHandle.getFormId(), _currentHandle.getAnchorId(),
					_currentDistance);
			_eProcedure.addInstruction(_currentInstruction, Position.After,
					_baseInstruction);
			_currentOffset.set(_cursor.getPosition().x - _currentHandle.getX(),
					_cursor.getPosition().y - _currentHandle.getY());
			_focus.setFocus(_currentInstruction);
		} else {
			_selectionTool.press();
			refreshHandles();
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
				_baseInstruction = null;
				_toolState.setActiveHandle(null);
				_toolState.setActiveSnapPoint(null);
			}
		} else {
			_selectionTool.release();
		}
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
		refreshHandles();
	}

	@Override
	public void toggleOption() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cycle() {
		if (_state == State.Snapped) {
			_cursor.cycleNextHandle();
		} else {
			_cursor.cycleNextSnap();
		}
		_selectionTool.cycle();

		refreshHandles();

	}

	@Override
	public void input(final Input input) {
		switch (_state) {
		case Idle:
		case Snapped: {
			_currentHandle = _cursor.getHandle(
					HitTester.EntityFilter.OnlySelected, HandleFilter.Any);
			if (_currentHandle == null) {
				_state = State.Idle;
			} else {
				_state = State.Snapped;
			}

			_toolState.setActiveHandle(_currentHandle);
			break;
		}
		case Pressed:
		case PressedSnapped: {
            SnapPoint currentSnapPoint = _cursor
                    .getSnapPoint(EntityFilter.ExcludeSelected);

			if (currentSnapPoint == null) {
				_state = State.Pressed;
				final Vec2 delta = new Vec2(_cursor.getPosition().x
						- _startPosition.x - _currentOffset.x,
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
					d.setReferenceB(currentSnapPoint.createReference());
				} else {
					d = new RelativeDistance(_currentHandle.createReference(),
							currentSnapPoint.createReference());
                }
				if (input.getShiftModifier().isActive()) {
					d.setDirection(getDirectionFor(_cursor.getX()
							- _startPosition.x, _cursor.getY()
							- _startPosition.y));
				} else {
					d.setDirection(FreeDirection.Free);
				}
				_currentDistance = d;
				_toolState.setActiveSnapPoint(currentSnapPoint);
			}
			_currentInstruction.setDistance(_currentDistance);

			_eProcedure.publishInstructionChange(_currentInstruction);
			break;
		}
		}

		_selectionTool.input(input);

		refreshHandles();
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

	private Direction getDirectionFor(final double x, final double y) {
		final double absX = x < 0 ? -x : x;
		final double absY = y < 0 ? -y : y;

		final double rel = absX == 0 ? absY : absY / absX;

		if (rel > 1) {
			return Direction.CartesianDirection.Vertical;
		} else {
			return Direction.CartesianDirection.Horizontal;
		}
	}

}
