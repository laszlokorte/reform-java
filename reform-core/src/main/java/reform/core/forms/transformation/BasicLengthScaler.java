package reform.core.forms.transformation;

import reform.core.forms.relations.StaticLength;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.RotationAngle;
import reform.math.Vector;

public class BasicLengthScaler implements Scaler
{

	private final StaticLength _length;
	private final RotationAngle _angle;
	private final double _angleOffset;

	public BasicLengthScaler(final StaticLength length, final RotationAngle angle, final
	double angleOffset)
	{
		_length = length;
		_angle = angle;
		_angleOffset = angleOffset;
	}

	@Override
	public void scale(final Runtime runtime, final double factor, final double fixX,
	                  final double fixY, final double directionX, final double
			                      directionY)
	{

		if (directionX == 0 && directionY == 0)
		{
			final double l = _length.getValueForRuntime(runtime);
			final double scaledLength = l * factor;

			_length.setForRuntime(runtime, scaledLength);
		}
		else
		{

			final double l = _length.getValueForRuntime(runtime);

			final double a = _angle.getValueForRuntime(runtime) - _angleOffset;
			final double x = Math.sin(a) * l;
			final double y = Math.cos(a) * l;

			final double deltaX = x - fixX;
			final double deltaY = y - fixY;

			final double projectedX = Vector.projectionX(deltaX, deltaY, directionX,
			                                             directionY);
			final double projectedY = Vector.projectionY(deltaX, deltaY, directionX,
			                                             directionY);

			final double scaledX = deltaX + projectedX * (factor - 1);
			final double scaledY = deltaY + projectedY * (factor - 1);

			final double scaledLength = Math.sqrt(
					scaledX * scaledX + scaledY * scaledY) / l;

			_length.setForRuntime(runtime, scaledLength);
		}

	}
}
