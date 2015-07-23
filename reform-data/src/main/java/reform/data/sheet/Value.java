package reform.data.sheet;

import java.util.Locale;

/**
 * Created by laszlokorte on 23.07.15.
 */
public final class Value
{
	public String asString() {
		switch (type) {
			case String:
				return "\""+_stringValue+"\"";
			case Integer:
				return _stringValue;
			case Double:
				return _stringValue;
			default:
				return "null";
		}
	}

	public String toString() {
		return asString();
	}

	public enum Type
	{
		String, Integer, Double
	}

	public final Type type;
	private final double _doubleValue;
	private final String _stringValue;
	private final int _integerValue;

	public Value(final int integer)
	{
		type = Type.Integer;
		_integerValue = integer;
		_doubleValue = integer;
		_stringValue = integer + "";
	}

	public Value(final double dbl)
	{
		type = Type.Double;
		_integerValue = (int) dbl;
		_doubleValue = dbl;
		_stringValue = String.format(Locale.ENGLISH, "%.2f", dbl);
	}

	public Value(final String string)
	{
		type = Type.String;
		_integerValue = 0;
		_doubleValue = 0;
		_stringValue = string;
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

}
