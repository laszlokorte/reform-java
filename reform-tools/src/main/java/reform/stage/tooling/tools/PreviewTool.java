package reform.stage.tooling.tools;

import reform.stage.tooling.Input;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;

public class PreviewTool implements Tool
{

	private final ToolState _toolState;

	public PreviewTool(final ToolState toolState)
	{
		_toolState = toolState;
	}

	@Override
	public void setUp()
	{
		_toolState.setViewState(ToolState.ViewState.Preview);
		_toolState.setSelectionState(ToolState.SelectionState.None);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void toggleOption()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void cycle()
	{

	}

	@Override
	public void input(final Input input)
	{

	}

}
