package reform.core.procedure.instructions;

public interface InstructionGroup extends Instruction, Iterable<Instruction> {
	public static enum Position {
		Before(0), After(1);

		public final int offset;

		private Position(final int offset) {
			this.offset = offset;
		}
	}

	public int size();

	public Instruction get(int index);

	public int indexOf(Instruction instruction);

	public void insertInstruction(Instruction instruction, Position pos,
			Instruction base);

	public void removeInstruction(Instruction instruction);

	public void append(Instruction readInstruction);
}
