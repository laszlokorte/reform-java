package reform.core.attributes;

import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class NumberValue
{
	private Expression _expression;

	public NumberValue(Expression expression) {
		_expression = expression;
	}

	public NumberValue(double value) {
		_expression = new ConstantExpression(new Value(value));
	}

	public double getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _expression.getValueFor(runtime.getDataSet()).getDouble();
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		_expression = expression;
	}
}
