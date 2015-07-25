package reform.core.forms.outline;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.RotationAngle;
import reform.math.Vector;

public class CircleOutline implements Outline
{

	private final ReferencePoint _center;
	private final Length _radius;
	private final RotationAngle _angle;

	public CircleOutline(final ReferencePoint center, final Length radius, final
	RotationAngle angle)
	{
		_center = center;
		_radius = radius;
		_angle = angle;
	}

	@Override
	public double getXForRuntime(final Runtime runtime, final double t)
	{
		final double radius = _radius.getValueForRuntime(runtime);
		final double angle = _angle.getValueForRuntime(runtime);
		return _center.getXValueForRuntime(runtime) + Vector.getRotatedX(radius, 0,
		                                                                 t * Math.PI * 2
				                                                                 +
				                                                                 angle);
	}

	@Override
	public double getYForRuntime(final Runtime runtime, final double t)
	{
		final double radius = _radius.getValueForRuntime(runtime);
		final double angle = _angle.getValueForRuntime(runtime);
		return _center.getYValueForRuntime(runtime) + Vector.getRotatedY(radius, 0,
		                                                                 t * Math.PI * 2
				                                                                 +
				                                                                 angle);
	}

	@Override
	public double getLengthForRuntime(final Runtime runtime)
	{
		final double radius = _radius.getValueForRuntime(runtime);
		return Vector.circumference(radius);
	}

	double getCenterXForRuntime(final Runtime runtime)
	{
		return _center.getXValueForRuntime(runtime);
	}

	double getCenterYForRuntime(final Runtime runtime)
	{
		return _center.getYValueForRuntime(runtime);
	}

	double getRadiusForRuntime(final Runtime runtime)
	{
		return _radius.getValueForRuntime(runtime);
	}

}
