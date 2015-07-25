package reform.stage.elements.controls;

import reform.core.procedure.instructions.single.RotateInstruction;
import reform.stage.elements.ControlPoint;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.RubberBand;

import java.util.ArrayList;
import java.util.List;

public class RotateInstructionControl implements InstructionControl
{
	private final RotateInstruction _instruction;
	private final ArrayList<ControlPoint> _controlPoints = new ArrayList<>();

	public RotateInstructionControl(final RotateInstruction instruction)
	{
		_instruction = instruction;
	}

	public RotateInstruction getInstruction()
	{
		return _instruction;
	}

	@Override
	public List<ControlPoint> getControlPoints()
	{
		return _controlPoints;
	}

	@Override
	public RubberBand getRubberBand()
	{
		return null;
	}

	@Override
	public void updateForRuntime(final reform.core.runtime.Runtime runtime)
	{

	}

	public boolean canEdit()
	{
		return false;
	}

}
