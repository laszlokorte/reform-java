package reform.data.sheet;

import java.util.Locale;

/**
 * Created by laszlokorte on 23.07.15.
 */
public final class Value
{

	public enum Type
	{
		String, Integer, Double, Boolean, Color
	}

	public final Type type;
	private final double _doubleValue;
	private final String _stringValue;
	private final int _integerValue;
	private final boolean _booleanValue;
	private final int _color;

	public Value()
	{
		type = null;
		_integerValue = 0;
		_doubleValue = Double.NaN;
		_stringValue = "null";
		_booleanValue = false;
		_color = 0xff000000;
	}

	public Value(final int integer)
	{
		this(integer, false);
	}

	public Value(final int integer, boolean isColor)
	{
		if (isColor)
		{
			type = Type.Color;
			_integerValue = 0;
			_doubleValue = 0;
			_stringValue = String.format("#%08X", integer);
			_booleanValue = false;
			_color = integer;
		}
		else
		{
			type = Type.Integer;
			_integerValue = integer;
			_doubleValue = integer;
			_stringValue = integer + "";
			_booleanValue = false;
			_color = 0xff000000;
		}
	}

	public Value(final double dbl)
	{
		type = Type.Double;
		_integerValue = (int) dbl;
		_doubleValue = dbl;
		_stringValue = String.format(Locale.ENGLISH, "%.2f", dbl);
		_booleanValue = false;
		_color = 0xff000000;

	}

	public Value(final String string)
	{
		type = Type.String;
		_integerValue = 0;
		_doubleValue = 0;
		_stringValue = string;
		_booleanValue = false;
		_color = 0xff000000;
	}

	public Value(final boolean bool)
	{
		type = Type.Boolean;
		_integerValue = 0;
		_doubleValue = 0;
		_stringValue = bool ? "true" : "false";
		_booleanValue = bool;
		_color = 0xff000000;
	}

	public Value( final double a, final double r, final double g, final double b)
	{
		type = Type.Color;
		_integerValue = 0;
		_doubleValue = 0;
		_booleanValue = false;
		_color = ((int) (255 * a) << 24) | ((int) (255 * r) << 16) | ((int) (255 * g) << 8) | ((int) (255 * b));
		_stringValue = String.format("#%08X", _color);

	}


	public int getInteger()
	{
		return _integerValue;
	}

	public double getDouble()
	{
		return _doubleValue;
	}

	public String getString()
	{
		return _stringValue;
	}

	public boolean getBoolean()
	{
		return _booleanValue;
	}

	public int getColor()
	{
		return _color;
	}

	public String asString()
	{
		switch (type)
		{
			case String:
				return "\"" + _stringValue + "\"";
			case Integer:
			case Double:
			case Boolean:
			case Color:
				return _stringValue;
			default:
				return "null";
		}
	}

	public String toString()
	{
		return asString();
	}
}
