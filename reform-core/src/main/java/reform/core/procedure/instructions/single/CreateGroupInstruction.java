package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.Procedure;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.runtime.*;
import reform.identity.Identifier;

public class CreateGroupInstruction implements Instruction {

    @Override
    public void setParent(InstructionGroup parent) {

    }

    @Override
    public InstructionGroup getParent() {
        return null;
    }

    @Override
    public void onAdded(Procedure procedure) {

    }

    @Override
    public void onRemoved(Procedure procedure) {

    }

    @Override
    public Identifier<? extends Form> getTarget() {
        return null;
    }

    @Override
    public void analyze(Analyzer analyzer) {

    }

    @Override
    public void evaluate(reform.core.runtime.Runtime runtime) {

    }
}
