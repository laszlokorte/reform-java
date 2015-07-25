package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public final class GreaterThanExpression extends BinaryExpressionBase implements Expression
{
	public GreaterThanExpression(final Expression lhs, final Expression rhs)
	{
		super(lhs, rhs);
	}

	@Override
	public String asString(final boolean parens)
	{
		final String s = _lhs.asString(false) + " > " + _rhs.asString(false);
		if (parens)
		{
			return "(" + s + ")";
		}
		else
		{
			return s;
		}
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		return Calculator.greaterThan(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
