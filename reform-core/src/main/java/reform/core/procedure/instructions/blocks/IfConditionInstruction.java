package reform.core.procedure.instructions.blocks;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.runtime.Runtime;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class IfConditionInstruction extends BaseInstructionGroup
{

	private Expression _condition = new ConstantExpression(new Value(true));

	@Override
	public void evaluate(final Runtime runtime)
	{
		if (_condition.getValueFor(runtime.getDataSet()).getBoolean())
		{
			_evaluateChildren(runtime);
		}
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		analyzer.publishGroup(this, "If " + _condition.asString(true) + ":");
		_analyzeChildren(analyzer);
	}

	public Expression getCondition()
	{
		return _condition;
	}

	public void setCondition(final Expression condition)
	{
		_condition = condition;
	}

}
