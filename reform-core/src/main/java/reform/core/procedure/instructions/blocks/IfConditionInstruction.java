package reform.core.procedure.instructions.blocks;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.runtime.Runtime;

public class IfConditionInstruction extends BaseInstructionGroup
{

	private boolean _condition = true;

	@Override
	public void evaluate(final Runtime runtime)
	{
		if (_condition)
		{
			_evaluateChildren(runtime);
		}
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		if (_condition)
		{
			analyzer.publishGroup(this, "If true:");
		}
		else
		{
			analyzer.publishGroup(this, "If false:");
		}
		_analyzeChildren(analyzer);
	}

	public boolean getCondition()
	{
		return _condition;
	}

	public void setCondition(final boolean condition)
	{
		_condition = condition;
	}

}
