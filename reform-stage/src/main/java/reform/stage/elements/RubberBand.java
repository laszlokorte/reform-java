package reform.stage.elements;

import reform.core.procedure.instructions.Instruction;
import reform.math.Vec2;

public class RubberBand
{
	private final ControlPoint _controlA;
	private final ControlPoint _controlB;

	public RubberBand(ControlPoint controlA, ControlPoint controlB)
	{
		_controlA = controlA;
		_controlB = controlB;
	}

	public double getStartX()
	{
		return _controlA.getX();
	}

	public double getStartY()
	{
		return _controlA.getY();
	}

	public double getEndX() {
		return _controlB.getX();
	}

	public double getEndY() {
		return _controlB.getY();
	}


}
