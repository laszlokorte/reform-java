package reform.stage.tooling.tools;

import reform.stage.tooling.Input;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;

public class RepairInstructionTool implements Tool
{

	private enum State
	{
		Idle
	}

	private final ToolState _toolState;

	private final State _state = State.Idle;

	public RepairInstructionTool(final ToolState toolState)
	{
		_toolState = toolState;
	}

	@Override
	public void setUp()
	{
		_toolState.setViewState(ToolState.ViewState.Control);
		_toolState.setSelectionState(ToolState.SelectionState.Control);
	}

	@Override
	public void tearDown()
	{
	}

	@Override
	public void cancel()
	{
	}

	@Override
	public void press()
	{

	}

	@Override
	public void release()
	{

	}

	@Override
	public void refresh()
	{
	}

	@Override
	public void toggleOption()
	{
	}

	@Override
	public void cycle()
	{
	}

	@Override
	public void input(final Input input)
	{
	}

	@Override
	public void focusChanged()
	{

	}

}
