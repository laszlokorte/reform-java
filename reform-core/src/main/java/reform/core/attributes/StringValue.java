package reform.core.attributes;

import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class StringValue
{
	private Expression _expression;

	public StringValue(Expression expression) {
		_expression = expression;
	}

	public StringValue(String value) {
		_expression = new ConstantExpression(new Value(value));
	}

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
