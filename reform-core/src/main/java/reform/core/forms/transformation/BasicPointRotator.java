package reform.core.forms.transformation;

import reform.core.forms.relations.StaticPoint;
import reform.core.runtime.Runtime;

public class BasicPointRotator implements Rotator
{

	private final StaticPoint[] _points;

	public BasicPointRotator(final StaticPoint... points)
	{
		_points = points;
	}

	@Override
	public void rotate(final Runtime runtime, final double angle, final double fixX, final double fixY)
	{

		for (int i = 0; i < _points.length; i++)
		{
			final double x = _points[i].getXValueForRuntime(runtime);
			final double y = _points[i].getYValueForRuntime(runtime);

			final double dx = x - fixX;
			final double dy = y - fixY;

			final double rotX = dx * Math.cos(angle) - dy * Math.sin(angle);
			final double rotY = dy * Math.cos(angle) + dx * Math.sin(angle);

			_points[i].setForRuntime(runtime, fixX + rotX, fixY + rotY);
		}
	}

}
