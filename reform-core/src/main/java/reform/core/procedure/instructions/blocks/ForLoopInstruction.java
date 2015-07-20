package reform.core.procedure.instructions.blocks;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.runtime.Runtime;

public class ForLoopInstruction extends BaseInstructionGroup
{

	private int _times;

	public ForLoopInstruction(final int times)
	{
		_times = times;
	}

	@Override
	public void evaluate(final Runtime runtime)
	{
		for (int i = 0; i < _times; i++)
		{
			if (runtime.shouldStop())
			{
				return;
			}
			_evaluateChildren(runtime);
		}

	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		analyzer.publishGroup(this, "Repeat " + _times + " times:");
		_analyzeChildren(analyzer);
	}

	public int getTimes()
	{
		return _times;
	}

	public void setTimes(final int times)
	{
		_times = times;
	}

}
