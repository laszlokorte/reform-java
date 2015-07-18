package reform.core.procedure.instructions;

import reform.core.procedure.Procedure;

public abstract class BaseInstruction implements Instruction {
	private InstructionGroup _parent;

	@Override
	final public void setParent(final InstructionGroup parent) {
		_parent = parent;
	}

	@Override
	final public InstructionGroup getParent() {
		return _parent;
	}

}
