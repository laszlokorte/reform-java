package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public final class LogicAndExpression extends BinaryExpressionBase implements Expression
{
	public LogicAndExpression(final Expression lhs, final Expression rhs)
	{
		super(lhs, rhs);
	}

	@Override
	public String asString(final boolean parens)
	{
		final String s = _lhs.asString(false) + " && " + _rhs.asString(false);
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
		return Calculator.logicAnd(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
