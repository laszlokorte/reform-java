package reform.core.attributes;

import reform.data.sheet.expression.Expression;

public class ExpressionScalarValue implements ScalarValue
{
	private Expression _expression;

	public ExpressionScalarValue(Expression expression) {
		_expression = expression;
	}

	@Override
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
