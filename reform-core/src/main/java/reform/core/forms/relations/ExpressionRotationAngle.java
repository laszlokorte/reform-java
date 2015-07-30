package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.RotationAngle;
import reform.data.sheet.expression.Expression;

public class ExpressionRotationAngle implements RotationAngle
{

	private Expression _angleExpression;

	public ExpressionRotationAngle(final Expression angleExpression)
	{
		_angleExpression = angleExpression;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime)
	{
		return _angleExpression.getValueFor(runtime.getDataSet()).getDouble() * 2 * Math.PI;
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return _angleExpression.asString(true);
	}

	@Override
	public boolean isDegenerated()
	{
		return false;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return true;
	}

	public Expression getExpression()
	{
		return _angleExpression;
	}

	public void setAngleExpression(final Expression angleExpression)
	{
		_angleExpression = angleExpression;
	}

}
