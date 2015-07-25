package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public class DivisionExpression extends BinaryExpressionBase implements Expression
{
	public DivisionExpression(final Expression lhs, final Expression rhs)
	{
		super(lhs, rhs);
	}

	@Override
	public String asString(final boolean parens)
	{
		final boolean leftParen = _lhs instanceof AdditionExpression || _lhs instanceof SubtractionExpression;
		final boolean rightParen = _rhs instanceof AdditionExpression || _rhs instanceof SubtractionExpression;
		final String op = " / ";
		final String s = _lhs.asString(leftParen) + op + _rhs.asString(rightParen);
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
		return Calculator.divide(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
