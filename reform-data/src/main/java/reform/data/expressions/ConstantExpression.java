package reform.data.expressions;

import reform.data.Expression;
import reform.data.ExpressionContext;
import reform.data.Value;

public class ConstantExpression implements Expression
{

	private final Value _value = new Value();

	@Override
	public Value evaluate(final ExpressionContext context)
	{
		return _value;
	}

}
