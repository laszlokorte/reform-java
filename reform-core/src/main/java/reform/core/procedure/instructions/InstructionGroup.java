package reform.core.procedure.instructions;

public interface InstructionGroup extends Instruction, Iterable<Instruction> {
	enum Position {
		Before(0), After(1);

		public final int offset;

		Position(final int offset) {
			this.offset = offset;
		}
	}

	int size();

	Instruction get(int index);

	int indexOf(Instruction instruction);

	void insertInstruction(Instruction instruction, Position pos,
                           Instruction base);

	void removeInstruction(Instruction instruction);

	void append(Instruction readInstruction);
}
