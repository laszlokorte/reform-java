package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.RotationAngle;

public class ComposedPolarPoint implements ReferencePoint
{

	private final Length _radius;
	private final RotationAngle _angle;

	public ComposedPolarPoint(final Length radius, final RotationAngle angle)
	{
		_radius = radius;
		_angle = angle;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return Math.cos(_angle.getValueForRuntime(runtime)) * _radius.getValueForRuntime(
				runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return Math.sin(_angle.getValueForRuntime(runtime)) * _radius.getValueForRuntime(
				runtime);
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return _radius.isValidFor(runtime) && _angle.isValidFor(runtime);
	}

}
