package reform.stage.elements.controls;

import reform.stage.elements.ControlPoint;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.RubberBand;

import java.util.ArrayList;
import java.util.List;

public class NullControl implements InstructionControl
{
	private final ArrayList<ControlPoint> _controlPoints = new ArrayList<>();


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
