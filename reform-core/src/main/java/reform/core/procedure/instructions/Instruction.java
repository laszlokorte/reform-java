package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzable;
import reform.core.forms.Form;
import reform.core.procedure.Procedure;
import reform.core.runtime.Evaluatable;
import reform.identity.Identifier;

public interface Instruction extends Evaluatable, Analyzable {

	void setParent(InstructionGroup parent);

	InstructionGroup getParent();

    Identifier<? extends Form> getTarget();
}
