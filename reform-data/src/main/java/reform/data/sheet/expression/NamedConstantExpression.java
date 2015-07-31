package reform.data.sheet.expression;


import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

public class NamedConstantExpression implements Expression
{
	private final String _name;
	private final Value _value;

	public NamedConstantExpression(final String name, final Value value)
	{
		_name = name;
		_value = value;
	}

	@Override
	public String asString(final boolean paren)
	{
		return _name;
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		return _value;
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{

	}

	@Override
	public String toString()
	{
		return _name;
	}

	public Value getValue()
	{
		return _value;
	}
}
