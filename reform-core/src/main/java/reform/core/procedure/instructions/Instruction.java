package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzable;
import reform.core.forms.Form;
import reform.core.procedure.Procedure;
import reform.core.runtime.Evaluatable;
import reform.identity.Identifier;

public interface Instruction extends Evaluatable, Analyzable {

	public void setParent(InstructionGroup parent);

	public InstructionGroup getParent();

	public void onAdded(Procedure procedure);

	public void onRemoved(Procedure procedure);

	public Identifier<? extends Form> getTarget();
}
