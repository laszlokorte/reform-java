package reform.core.procedure.instructions.blocks;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.InvalidExpressionError;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;

public class ForLoopInstruction extends BaseInstructionGroup
{

	private Expression _times;

	public ForLoopInstruction(final int times)
	{
		_times = new ConstantExpression(new Value(times));
	}

	@Override
	public void evaluate(final Runtime runtime)
	{
		Value iterations = _times.getValueFor(runtime.getDataSet());

		if(iterations.type != Value.Type.Integer) {
			runtime.reportError(this, new InvalidExpressionError(_times));
			return;
		}

		int count = iterations.getInteger();

		for (int i = 0; i < count; i++)
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
		analyzer.publishGroup(this, "Repeat " + _times.asString(false) + " times:");
		_analyzeChildren(analyzer);
	}

	public int getTimes()
	{
		return ((ConstantExpression)_times).getValue().getInteger();
	}

	public void setTimes(final int times)
	{
		_times = new ConstantExpression(new Value(times));
	}

	public Expression getExpression()
	{
		return _times;
	}

	public void setExpression(final Expression expression)
	{
		_times = expression;
	}
}
