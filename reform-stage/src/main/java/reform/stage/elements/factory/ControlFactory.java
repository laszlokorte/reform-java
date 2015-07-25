package reform.stage.elements.factory;

import reform.core.procedure.instructions.single.MorphInstruction;
import reform.core.procedure.instructions.single.RotateInstruction;
import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.core.procedure.instructions.single.TranslateInstruction;
import reform.core.runtime.Evaluable;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.controls.*;

public class ControlFactory
{

	public InstructionControl createEntityFor(final Evaluable instruction)
	{
		if (instruction.getClass() == MorphInstruction.class)
		{
			return new MorphInstructionControl((MorphInstruction) instruction);
		}
		else if (instruction.getClass() == TranslateInstruction.class)
		{
			return new TranslateInstructionControl((TranslateInstruction) instruction);
		}
		else if (instruction.getClass() == ScaleInstruction.class)
		{
			return new ScaleInstructionControl((ScaleInstruction) instruction);
		}
		else if (instruction.getClass() == RotateInstruction.class)
		{
			return new RotateInstructionControl((RotateInstruction) instruction);
		}


		return new NullControl();
	}

}
