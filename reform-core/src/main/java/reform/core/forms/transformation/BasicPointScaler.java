package reform.core.forms.transformation;

import reform.core.forms.relations.StaticPoint;
import reform.core.runtime.Runtime;
import reform.math.Vector;

public class BasicPointScaler implements Scaler
{

	private final StaticPoint[] _points;

	public BasicPointScaler(final StaticPoint... points)
	{
		_points = points;
	}

	@Override
	public void scale(final Runtime runtime, final double factor, final double fixX, final double fixY, final double
			directionX, final double directionY)
	{

		for (int i = 0; i < _points.length; i++)
		{
			final double x = _points[i].getXValueForRuntime(runtime);
			final double y = _points[i].getYValueForRuntime(runtime);

			final double deltaX = x - fixX;
			final double deltaY = y - fixY;

			final double projectedX = Vector.projectionX(deltaX, deltaY, directionX, directionY);
			final double projectedY = Vector.projectionY(deltaX, deltaY, directionX, directionY);

			final double scaledX = deltaX + projectedX * (factor - 1);
			final double scaledY = deltaY + projectedY * (factor - 1);

			_points[i].setForRuntime(runtime, fixX + scaledX, fixY + scaledY);
		}

	}

}
