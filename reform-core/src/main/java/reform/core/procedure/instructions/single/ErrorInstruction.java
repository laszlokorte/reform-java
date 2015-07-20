package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;

public final class ErrorInstruction extends BaseInstruction
{

	public ErrorInstruction()
	{
	}

	@Override
	public void evaluate(final Runtime runtime)
	{
		runtime.reportError(this, new Error("Some artificial error for testing the runtime."));
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		analyzer.publish(this, "Throw Error");
	}

	@Override
	public Identifier<? extends Form> getTarget()
	{
		return null;
	}

}
