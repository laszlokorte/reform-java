package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzable;
import reform.core.forms.Form;
import reform.core.runtime.Evaluable;
import reform.identity.Identifier;

public interface Instruction extends Evaluable, Analyzable
{

	InstructionGroup getParent();

	void setParent(InstructionGroup parent);

	Identifier<? extends Form> getTarget();
}
