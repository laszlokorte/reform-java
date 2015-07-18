package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;

public final class NullInstruction extends BaseInstruction {

	public NullInstruction(final InstructionGroup parent) {
		setParent(parent);
	}

	public NullInstruction() {
	}

	@Override
	public void evaluate(final Runtime runtime) {

	}

	@Override
	public void analyze(final Analyzer analyzer) {
		analyzer.publish(this, "Null");
	}

	@Override
	public Identifier<? extends Form> getTarget() {
		return null;
	}

}
