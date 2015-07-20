package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.RotationAngle;
import reform.math.Vector;

public class RotatedPoint implements ReferencePoint
{

	private final ReferencePoint _point;
	private final RotationAngle _angle;

	public RotatedPoint(final ReferencePoint point, final RotationAngle angle)
	{
		_point = point;
		_angle = angle;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		final double angle = _angle.getValueForRuntime(runtime);
		return Vector.getRotatedX(_point.getXValueForRuntime(runtime), _point.getYValueForRuntime(runtime), angle);

	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		final double angle = _angle.getValueForRuntime(runtime);
		return Vector.getRotatedY(_point.getXValueForRuntime(runtime), _point.getYValueForRuntime(runtime), angle);
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return null;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return _point.isValidFor(runtime) && _angle.isValidFor(runtime);
	}

}
