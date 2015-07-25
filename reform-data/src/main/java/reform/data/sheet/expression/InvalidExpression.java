package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

public final class InvalidExpression implements Expression
{
	private final static Value NULL = new Value(0);

	private final CharSequence _expressionString;

	public InvalidExpression(final CharSequence string)
	{
		_expressionString = string;
	}

	@Override
	public String asString(final boolean parens)
	{
		if (parens)
		{
			return "(" + _expressionString + ")";
		}
		else
		{
			return _expressionString.toString();
		}
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		return NULL;
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{

	}
}
