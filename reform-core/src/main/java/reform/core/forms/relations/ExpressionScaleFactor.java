package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ScaleFactor;
import reform.data.sheet.expression.Expression;

public class ExpressionScaleFactor implements ScaleFactor
{

	private Expression _factorExpression;

	public ExpressionScaleFactor(final Expression factorExpression)
	{
		_factorExpression = factorExpression;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime)
	{
		return _factorExpression.getValueFor(runtime.getDataSet()).getDouble();
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return true;
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return _factorExpression.asString(false);
	}

	public Expression getExpression()
	{
		return _factorExpression;
	}

	@Override
	public boolean isDegenerated()
	{
		return false;
	}

	public void setFactorExpression(final Expression factorExpression)
	{
		_factorExpression = factorExpression;
	}

}
