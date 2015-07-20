package reform.stage.tooling;

import reform.stage.tooling.cursor.Cursor;
import reform.stage.tooling.modifiers.Modifier;

public class Input
{
	private final Cursor _cursor;
	private final Modifier _shift;
	private final Modifier _alt;

	public Input(final Cursor cursor, final Modifier shift, final Modifier alt)
	{
		_cursor = cursor;
		_shift = shift;
		_alt = alt;
	}

	public Cursor getCursor()
	{
		return _cursor;
	}

	public Modifier getShiftModifier()
	{
		return _shift;
	}

	public Modifier getAltModifier()
	{
		return _alt;
	}
}
