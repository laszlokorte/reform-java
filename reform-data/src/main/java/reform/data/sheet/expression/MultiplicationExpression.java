package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public class MultiplicationExpression extends BinaryExpressionBase implements Expression
{

	public MultiplicationExpression(final Expression lhs, final Expression rhs)
	{
		super(lhs, rhs);
	}

	@Override
	public String asString(final boolean parens)
	{
		final boolean leftParen = _lhs instanceof AdditionExpression || _lhs instanceof
				SubtractionExpression;
		final boolean rightParen = _rhs instanceof AdditionExpression || _rhs instanceof
				SubtractionExpression;
		final String op = " * ";
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
		return Calculator.multiply(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}

}
