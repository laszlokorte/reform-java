package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public final class UnaryPlusExpression extends UnaryExpressionBase implements Expression
{

	public UnaryPlusExpression(final Expression inner)
	{
		super(inner);
	}


	@Override
	public String asString(final boolean parens)
	{
		final String s = "+" + _inner.asString(
				!(_inner instanceof ConstantExpression || _inner instanceof
						ReferenceExpression || _inner instanceof ExponentialExpression));
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
		return _inner.getValueFor(set);
	}

}
