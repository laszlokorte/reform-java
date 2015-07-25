package reform.core.procedure;

import reform.core.analyzer.Analyzer;
import reform.core.procedure.instructions.BaseInstructionGroup;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.procedure.instructions.NullInstruction;
import reform.core.runtime.Runtime;
import reform.math.Vec2i;

public class Procedure
{
	private final BaseInstructionGroup _root = new RootInstruction();
	private transient final Paper _paper = new Paper();

	public Procedure()
	{
	}

	public void evaluate(final Runtime runtime)
	{
		//synchronized (runtime) {
		runtime.begin();

		runtime.pushScope();
		runtime.declare(_paper);
		final Vec2i size = runtime.getSize();
		_paper.initialize(runtime, 0, 0, size.x, size.y);

		_root.evaluate(runtime);
		runtime.popScope();
		runtime.finish();
		//}
	}

	public void analyze(final Analyzer analyzer)
	{
		analyzer.begin();

		analyzer.announceForm(_paper);
		_root.analyze(analyzer);

		analyzer.finish();
	}

	public void addInstruction(final Instruction instruction, final InstructionGroup
			.Position pos, final Instruction base)
	{
		final InstructionGroup parent = base.getParent();
		if (base instanceof InstructionGroup)
		{
			final int index = parent.indexOf(base) + 1;
			if (index < parent.size() && parent.get(index) instanceof NullInstruction)
			{
				addInstruction(instruction, pos, parent.get(index));
				return;
			}
		}
		parent.insertInstruction(instruction, pos, base);

		if (instruction instanceof InstructionGroup)
		{
			addInstruction(new NullInstruction(), Position.After, instruction);
		}
	}

	public void removeInstruction(final Instruction instruction)
	{
		final InstructionGroup parent = instruction.getParent();
		if (instruction instanceof InstructionGroup)
		{
			final int index = parent.indexOf(instruction) + 1;
			if (index < parent.size())
			{
				final Instruction nullAfter = parent.get(index);
				if (nullAfter instanceof NullInstruction)
				{
					parent.removeInstruction(nullAfter);
				}
			}
		}
		parent.removeInstruction(instruction);
	}

	public void __dbg__addInstruction(final Instruction instruction)
	{
		_root.insertInstruction(instruction, Position.After, _root.get(_root.size() -
				                                                               1));
	}

	public Paper getPaper()
	{
		return _paper;
	}

	public InstructionGroup getRoot()
	{
		return _root;
	}

	public boolean canRemoveInstruction(final Instruction instruction)
	{
		return !(instruction instanceof NullInstruction) && !(instruction instanceof
				RootInstruction);
	}
}
