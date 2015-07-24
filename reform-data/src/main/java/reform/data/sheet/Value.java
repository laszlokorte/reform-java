package reform.data.sheet;

import java.util.Locale;

/**
 * Created by laszlokorte on 23.07.15.
 */
public final class Value
{

	public enum Type
	{
		String, Integer, Double, Boolean
	}

	public final Type type;
	private final double _doubleValue;
	private final String _stringValue;
	private final int _integerValue;
	private final boolean _booleanValue;

	public Value()
	{
		type = null;
		_integerValue = 0;
		_doubleValue = Double.NaN;
		_stringValue = "null";
		_booleanValue = false;
	}

	public Value(final int integer)
	{
		type = Type.Integer;
		_integerValue = integer;
		_doubleValue = integer;
		_stringValue = integer + "";
		_booleanValue = false;
	}

	public Value(final double dbl)
	{
		type = Type.Double;
		_integerValue = (int) dbl;
		_doubleValue = dbl;
		_stringValue = String.format(Locale.ENGLISH, "%.2f", dbl);
		_booleanValue = false;

	}

	public Value(final String string)
	{
		type = Type.String;
		_integerValue = 0;
		_doubleValue = 0;
		_stringValue = string;
		_booleanValue = false;
	}

	public Value(final boolean bool)
	{
		type = Type.Boolean;
		_integerValue = 0;
		_doubleValue = 0;
		_stringValue = bool ? "true" : "false";
		_booleanValue = bool;
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

	public String asString() {
		switch (type) {
			case String:
				return "\""+_stringValue+"\"";
			case Integer:
			case Double:
			case Boolean:
				return _stringValue;
			default:
				return "null";
		}
	}

	public String toString() {
		return asString();
	}
}
