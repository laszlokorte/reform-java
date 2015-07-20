package reform.core.procedure;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.runtime.Runtime;

public class RootInstruction extends BaseInstructionGroup
{

	@Override
	public void evaluate(final Runtime runtime)
	{
		try
		{
			runtime.pushScope();
			_evaluateChildren(runtime);
		} finally
		{
			runtime.popScope();
		}
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		_analyzeChildren(analyzer);
	}

}
