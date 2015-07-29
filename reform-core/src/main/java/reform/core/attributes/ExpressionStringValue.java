package reform.core.attributes;

import reform.data.sheet.expression.Expression;

public class ExpressionStringValue implements StringValue
{
	private Expression _expression;

	public ExpressionStringValue(Expression expression) {
		_expression = expression;
	}

	@Override
	public String getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _expression.getValueFor(runtime.getDataSet()).getString();
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		_expression = expression;
	}
}
