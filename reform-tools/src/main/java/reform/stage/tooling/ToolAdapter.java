package reform.stage.tooling;

import reform.core.forms.Form;
import reform.core.procedure.instructions.Instruction;
import reform.identity.Identifier;

public interface ToolAdapter
{

	Identifier<? extends Form> getSelectedFormId();

	Instruction getCurrentInstruction();

}
