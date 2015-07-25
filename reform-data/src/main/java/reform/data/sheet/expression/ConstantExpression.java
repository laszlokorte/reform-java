package reform.data.sheet.expression;


import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

public class ConstantExpression implements Expression
{
	private final Value _value;

	public ConstantExpression(final Value value)
	{
		_value = value;
	}

	@Override
	public String asString(final boolean paren)
	{
		return _value.asString();
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
		return _value.asString();
	}

	public Value getValue()
	{
		return _value;
	}
}
