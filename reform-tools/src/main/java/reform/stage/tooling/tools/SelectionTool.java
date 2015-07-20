package reform.stage.tooling.tools;

import reform.stage.Stage;
import reform.stage.elements.Entity;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.Input;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

public class SelectionTool implements Tool
{

	private enum State
	{
		Idle, Pressed

	}

	private final ToolState _toolState;
	private final FormSelection _formSelection;
	private final Cursor _cursor;
	private final Stage _stage;

	private State _state = State.Idle;

	public SelectionTool(final ToolState toolState, final FormSelection formSelection, final Cursor cursor, final
	Stage stage)
	{
		_toolState = toolState;
		_formSelection = formSelection;
		_cursor = cursor;
		_stage = stage;
	}

	@Override
	public void setUp()
	{
		_toolState.setViewState(ToolState.ViewState.Selection);
		_toolState.setSelectionState(ToolState.SelectionState.None);
	}

	@Override
	public void tearDown()
	{
	}

	@Override
	public void cancel()
	{
		_formSelection.setSelection(null);
	}

	@Override
	public void press()
	{
		_state = State.Pressed;

		if (_formSelection.isSet() && _stage.getEntityForId(_formSelection.getSelected()).contains(_cursor.getPosition
				()))
		{
			_toolState.setSelectionState(ToolState.SelectionState.Form);

			return;
		}

		final Entity e = _cursor.getEntity();

		if (e != null)
		{
			_formSelection.setSelection(e.getId());
			_toolState.setSelectionState(ToolState.SelectionState.Form);
		}
		else
		{
			_formSelection.reset();
		}
	}

	@Override
	public void release()
	{
		_state = State.Idle;
		_toolState.setSelectionState(ToolState.SelectionState.None);
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

		if (_state == State.Pressed)
		{
			_cursor.cycleNextEntity();

		}
	}

	@Override
	public void input(final Input input)
	{
		if (_state == State.Pressed)
		{
			final Entity e = _cursor.getEntity();

			if (e != null)
			{
				_formSelection.setSelection(e.getId());
			}

			_toolState.notifyChange();
		}
	}

}
