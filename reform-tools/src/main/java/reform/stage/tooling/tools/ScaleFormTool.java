package reform.stage.tooling.tools;

import reform.core.forms.relations.ConstantScaleFactor;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.core.runtime.relations.ReferencePoint;
import reform.evented.core.EventedProcedure;
import reform.math.Vec2;
import reform.math.Vector;
import reform.stage.elements.Handle;
import reform.stage.elements.PivotPair;
import reform.stage.elements.PivotPair.Choice;
import reform.stage.hittest.HitTester;
import reform.stage.hittest.HitTester.EntityFilter;
import reform.stage.hittest.HitTester.HandleFilter;
import reform.stage.tooling.Input;
import reform.stage.tooling.InstructionFocus;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

public class ScaleFormTool implements Tool
{

	private final SelectionTool _selectionTool;
	private final ToolState _toolState;
	private final Cursor _cursor;
	private final HitTester _hitTester;
	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;
	private final Vec2 _pivotPrimaryPos = new Vec2();
	private final Vec2 _pivotSecondaryPos = new Vec2();
	private final Vec2 _currentOffset = new Vec2();
	private final Vec2 _startPosition = new Vec2();
	private State _state = State.Idle;
	private Choice _pivotChoice = Choice.Primary;
	private ReferencePoint _pivotPrimary;
	private ReferencePoint _pivotSecondary;
	private Handle _currentHandle;
	private ScaleInstruction _currentInstruction;
	private ConstantScaleFactor _currentFactor;
	private Instruction _baseInstruction;
	public ScaleFormTool(final SelectionTool selectionTool, final ToolState toolState,
	                     final Cursor cursor, final HitTester hitTester, final
	                     InstructionFocus focus, final EventedProcedure eProcedure)
	{
		_selectionTool = selectionTool;
		_toolState = toolState;
		_cursor = cursor;
		_hitTester = hitTester;
		_focus = focus;
		_eProcedure = eProcedure;
	}

	@Override
	public void setUp()
	{
		_toolState.setViewState(ToolState.ViewState.Handle);
		_toolState.setSelectionState(ToolState.SelectionState.Handle);

		refreshHandles();
	}

	@Override
	public void tearDown()
	{
		_toolState.setPivot(null);
	}

	@Override
	public void cancel()
	{
		if (_state == State.Pressed)
		{
			_state = State.Idle;
			_eProcedure.removeInstruction(_currentInstruction);
			_currentInstruction = null;
			_toolState.setActiveHandle(null);
			_toolState.setPivot(null);
			_baseInstruction = null;
		}
		else
		{
			_selectionTool.cancel();
			_toolState.clearHandles();
		}

		_toolState.setSelectionState(ToolState.SelectionState.Handle);
	}

	@Override
	public void press()
	{
		if (_state == State.Snapped)
		{
			_state = State.Pressed;
			_startPosition.set(_currentHandle.getX(), _currentHandle.getY());
			_baseInstruction = _focus.getFocused();
			_currentFactor = new ConstantScaleFactor(1);
			final ReferencePoint pivotPoint = _pivotChoice == Choice.Primary ?
					_pivotPrimary : _pivotSecondary;

			_currentInstruction = new ScaleInstruction(_currentHandle.getFormId(),
			                                           _currentFactor, pivotPoint);
			_eProcedure.addInstruction(_currentInstruction, Position.After,
			                           _baseInstruction);
			_currentOffset.set(_cursor.getPosition().x - _currentHandle.getX(),
			                   _cursor.getPosition().y - _currentHandle.getY());
			_focus.setFocus(_currentInstruction);

			_toolState.setSelectionState(ToolState.SelectionState.None);
		}
		else
		{
			_selectionTool.press();
			refreshHandles();
		}
	}

	@Override
	public void release()
	{
		if (_state == State.Pressed)
		{
			if (_currentFactor.isDegenerated())
			{
				cancel();
			}
			else
			{
				_state = State.Idle;

				_currentInstruction = null;
				_baseInstruction = null;
				_toolState.setActiveHandle(null);
			}
		}
		else
		{
			_selectionTool.release();
		}

		_toolState.setSelectionState(ToolState.SelectionState.Handle);
	}

	@Override
	public void refresh()
	{
		_selectionTool.refresh();
		refreshHandles();

	}

	@Override
	public void toggleOption()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void cycle()
	{
		if (_state == State.Snapped)
		{
			_cursor.cycleNextHandle();
		}
		else
		{
			_cursor.cycleNextSnap();
		}
		_selectionTool.cycle();

		refreshHandles();

	}

	@Override
	public void input(final Input input)
	{
		_pivotChoice = input.getAltModifier().isActive() ? Choice.Secondary : Choice
				.Primary;

		switch (_state)
		{
			case Idle:
			case Snapped:
			{
				_currentHandle = _cursor.getHandle(HitTester.EntityFilter.OnlySelected,
				                                   HandleFilter.Pivot);
				if (_currentHandle == null)
				{
					_state = State.Idle;
				}
				else
				{
					final PivotPair pivotPair = _currentHandle.getPivot();
					_pivotPrimary = pivotPair.createReference(Choice.Primary);
					_pivotSecondary = pivotPair.createReference(Choice.Secondary);
					_pivotPrimaryPos.set(pivotPair.getX(Choice.Primary),
					                     pivotPair.getY(Choice.Primary));
					_pivotSecondaryPos.set(pivotPair.getX(Choice.Secondary),
					                       pivotPair.getY(Choice.Secondary));
					_state = State.Snapped;
				}

				_toolState.setActiveHandle(_currentHandle);
				break;
			}
			case Pressed:
			{
				_currentFactor.setFactor(calcFactor(input.getShiftModifier().isActive
						()));
				_currentInstruction.setFixPoint(
						_pivotChoice == Choice.Primary ? _pivotPrimary :
								_pivotSecondary);

				_eProcedure.publishInstructionChange(_currentInstruction);
				break;
			}
		}

		if (_state != State.Idle)
		{
			_toolState.setPivot(
					_pivotChoice == Choice.Primary ? _pivotPrimaryPos :
							_pivotSecondaryPos);
		}
		else
		{
			_toolState.setPivot(null);
		}

		_selectionTool.input(input);

		refreshHandles();
	}

	@Override
	public void focusChanged()
	{
		if (_focus.getFocused() != _currentInstruction)
		{
			cancel();
		}
		_selectionTool.focusChanged();

	}

	private void refreshHandles()
	{
		_toolState.setHandles(
				_hitTester.getAllHandles(EntityFilter.OnlySelected, HandleFilter.Pivot));
	}

	private double calcFactor(final boolean stepped)
	{
		final Vec2 pivotPos = _pivotChoice == Choice.Primary ? _pivotPrimaryPos :
				_pivotSecondaryPos;

		final double startPosX = _startPosition.x;
		final double startPosY = _startPosition.y;

		final double currentX = _cursor.getX() - _currentOffset.x;
		final double currentY = _cursor.getY() - _currentOffset.y;

		final double deltaStartX = startPosX - pivotPos.x;
		final double deltaStartY = startPosY - pivotPos.y;

		final double deltaCurrentX = currentX - pivotPos.x;
		final double deltaCurrentY = currentY - pivotPos.y;

		final double deltaProjectedX = Vector.projectionX(deltaCurrentX, deltaCurrentY,
		                                                  deltaStartX, deltaStartY);
		final double deltaProjectedY = Vector.projectionY(deltaCurrentX, deltaCurrentY,
		                                                  deltaStartX, deltaStartY);

		final double startDistance = Vector.length(deltaStartX, deltaStartY);
		final double projectedDistance = Vector.length(deltaProjectedX, deltaProjectedY);

		if (startDistance == 0)
		{
			return 0;
		}

		final double factor = Math.signum(
				Vector.dot(deltaProjectedX, deltaProjectedY, deltaStartX,
				           deltaStartY)) * projectedDistance / startDistance;

		return stepped ? Vector.inStepsOf(factor, 0.1) : factor;

	}

	private enum State
	{
		Idle, Snapped, Pressed
	}
}
