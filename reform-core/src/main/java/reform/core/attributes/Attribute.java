package reform.core.attributes;

import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class Attribute<E>
{
	private final String _name;
	private final Class<? super E> _type;
	private E _value;
	public Attribute(final String name, final Class<?super E> type, final E initialValue)
	{
		_name = name;
		_type = type;
		_value = initialValue;
	}

	public String getName()
	{
		return _name;
	}

	public Class<?super E> getType()
	{
		return _type;
	}

	public E getValue()
	{
		return _value;
	}

	public void setValue(final E newValue)
	{
		_value = newValue;
	}
}
