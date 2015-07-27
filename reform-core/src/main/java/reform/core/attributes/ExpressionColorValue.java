package reform.core.attributes;

import reform.data.sheet.expression.Expression;

public class ExpressionColorValue implements ColorValue
{
	private Expression _expression;

	@Override
	public int getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _expression.getValueFor(runtime.getDataSet()).getColor();
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		_expression = expression;
	}
}
