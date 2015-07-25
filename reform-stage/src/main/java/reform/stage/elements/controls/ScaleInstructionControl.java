package reform.stage.elements.controls;

import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.stage.elements.ControlPoint;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.RubberBand;

import java.util.ArrayList;
import java.util.List;

public class ScaleInstructionControl implements InstructionControl
{
	private final ScaleInstruction _instruction;
	private final ArrayList<ControlPoint> _controlPoints = new ArrayList<>();

	public ScaleInstructionControl(final ScaleInstruction instruction)
	{
		_instruction = instruction;
	}

	public ScaleInstruction getInstruction()
	{
		return _instruction;
	}

	@Override
	public void updateForRuntime(final reform.core.runtime.Runtime runtime)
	{

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

	public boolean canEdit()
	{
		return false;
	}
}
