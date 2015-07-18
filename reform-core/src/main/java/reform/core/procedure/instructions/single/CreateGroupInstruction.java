package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.identity.Identifier;

public class CreateGroupInstruction implements Instruction {

    @Override
    public void setParent(final InstructionGroup parent) {

    }

    @Override
    public InstructionGroup getParent() {
        return null;
    }

    @Override
    public Identifier<? extends Form> getTarget() {
        return null;
    }

    @Override
    public void analyze(final Analyzer analyzer) {

    }

    @Override
    public void evaluate(final reform.core.runtime.Runtime runtime) {

    }
}
