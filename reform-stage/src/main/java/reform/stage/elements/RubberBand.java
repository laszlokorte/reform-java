package reform.stage.elements;

public class RubberBand
{
	private final ControlPoint _controlA;
	private final ControlPoint _controlB;

	public RubberBand(final ControlPoint controlA, final ControlPoint controlB)
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

	public double getEndX()
	{
		return _controlB.getX();
	}

	public double getEndY()
	{
		return _controlB.getY();
	}


}
