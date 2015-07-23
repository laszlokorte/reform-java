package reform.stage.tooling;

import reform.stage.tooling.cursor.Cursor;
import reform.stage.tooling.modifiers.BasicModifier;
import reform.stage.tooling.tools.NullTool;

import java.util.ArrayList;
import java.util.List;

public class ToolController
{
	private final List<ToolControllerListener> _listeners = new ArrayList<>();
	private final Cursor _cursor;
	private Tool _tool = new NullTool();
	private Tool _nextTool = null;
	private boolean _pressed = false;

	private final BasicModifier _shift = new BasicModifier();
	private final BasicModifier _alt = new BasicModifier();
	private final Input _input;

	public ToolController(final Cursor cursor)
	{
		_cursor = cursor;
		_input = new Input(cursor, _shift, _alt);
	}

	public void selectTool(final Tool tool)
	{
		if (!_pressed)
		{
			setTool(tool);
		}
		else
		{
			_nextTool = tool;
		}
	}

	private void setTool(final Tool tool)
	{
		if (tool != _tool)
		{
			if (_tool != null)
			{
				_tool.tearDown();
			}
			_cursor.resetCycle();
			_tool = tool;
			_tool.setUp();
			_tool.refresh();
			notifyChange();
		}
	}

	public void setShift(final boolean isPressed)
	{
		_shift.setState(isPressed);
		_tool.input(_input);
	}

	public void setAlt(final boolean isPressed)
	{
		_alt.setState(isPressed);
		_tool.input(_input);
	}

	public void moveTo(final int x, final int y)
	{
		_cursor.setPosition(x, y);
		_tool.input(_input);
	}

	public void press()
	{
		_cursor.resetCycle();
		_pressed = true;
		_tool.press();
	}

	public void release()
	{
		_cursor.resetCycle();

		_pressed = false;
		_tool.release();
		if (_nextTool != null)
		{
			setTool(_nextTool);
			_nextTool = null;
		}
		_tool.input(_input);
	}

	public void cancel()
	{
		_cursor.resetCycle();
		_tool.cancel();
		_tool.input(_input);
	}

	public void cycleNext()
	{
		_tool.cycle();
		_tool.input(_input);
	}

	public void toggle()
	{
		_tool.toggleOption();
		_tool.input(_input);
	}

	public void refresh()
	{
		_tool.refresh();
	}

	public boolean isActiveTool(final Tool tool)
	{
		return _tool == tool;
	}

	public void addListener(final ToolControllerListener l)
	{
		_listeners.add(l);
	}

	public void removeListener(final ToolControllerListener l)
	{
		_listeners.remove(l);
	}

	private void notifyChange()
	{
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onToolChange(this);
		}
	}

	public Tool getTool()
	{
		return _tool;
	}

	public void onFocusChanged()
	{
		if(_tool!=null) {
			_tool.focusChanged();
		}
	}
}
