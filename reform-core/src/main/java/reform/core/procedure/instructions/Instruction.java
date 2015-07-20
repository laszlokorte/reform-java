package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzable;
import reform.core.forms.Form;
import reform.core.runtime.Evaluable;
import reform.identity.Identifier;

public interface Instruction extends Evaluable, Analyzable
{

	void setParent(InstructionGroup parent);

	InstructionGroup getParent();

	Identifier<? extends Form> getTarget();
}
