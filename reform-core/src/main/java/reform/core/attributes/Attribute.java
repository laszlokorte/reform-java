package reform.core.attributes;

import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class Attribute
{
	private final String _name;
	private final Type _type;
	private Expression _value;
	public Attribute(final String name, final Type type, final Value initialValue)
	{
		_name = name;
		_type = type;
		_value = new ConstantExpression(initialValue);
	}

	public String getName()
	{
		return _name;
	}

	public Type getType()
	{
		return _type;
	}

	public Expression getValue()
	{
		return _value;
	}

	public void setValue(final Expression newValue)
	{
		_value = newValue;
	}

	public enum Type
	{
		Number, Color, String
	}
}
