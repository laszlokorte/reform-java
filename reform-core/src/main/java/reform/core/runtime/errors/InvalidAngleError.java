package reform.core.runtime.errors;

import reform.core.runtime.relations.RotationAngle;

public class InvalidAngleError implements RuntimeError
{
	private final RotationAngle _angle;

	public InvalidAngleError(final RotationAngle angle)
	{
		_angle = angle;
	}

	public RotationAngle getAngle()
	{
		return _angle;
	}

	@Override
	public String getMessage()
	{
		return "Invalid Angle";
	}
}
