package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public final class UnaryMinusExpression extends UnaryExpressionBase implements Expression
{

	public UnaryMinusExpression(final Expression inner)
	{
		super(inner);
	}


	@Override
	public String asString(final boolean parens)
	{
		final String s = "-" + _inner.asString(
				!(_inner instanceof ConstantExpression || _inner instanceof
						ReferenceExpression ||
						_inner instanceof ExponentialExpression));
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
		return Calculator.negate(_inner.getValueFor(set));
	}

	public static Expression wrapOrSimplify(final Expression operand)
	{
		if (operand instanceof UnaryMinusExpression)
		{
			return ((UnaryMinusExpression) operand)._inner;
		}
		else if (operand instanceof ConstantExpression)
		{
			final ConstantExpression e = (ConstantExpression) operand;
			final Value v = e.getValue();
			if (v.type == Value.Type.Integer || v.type == Value.Type.Double)
			{
				return new ConstantExpression(Calculator.negate(v));
			}
		}
		return new UnaryMinusExpression(operand);

	}
}
