package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public final class LogicNegateExpression extends UnaryExpressionBase implements Expression
{

	public LogicNegateExpression(final Expression inner)
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
		return Calculator.logicNegate(_inner.getValueFor(set));
	}

	public static Expression wrapOrSimplify(final Expression operand)
	{
		if (operand instanceof LogicNegateExpression)
		{
			return ((LogicNegateExpression) operand)._inner;
		}
		else if (operand instanceof ConstantExpression)
		{
			final ConstantExpression e = (ConstantExpression) operand;
			final Value v = e.getValue();
			if (v.type == Value.Type.Boolean)
			{
				return new ConstantExpression(Calculator.logicNegate(v));
			}
		}
		return new LogicNegateExpression(operand);

	}
}
