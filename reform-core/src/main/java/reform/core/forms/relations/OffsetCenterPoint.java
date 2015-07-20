package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;
import reform.core.runtime.relations.ReferencePoint;
import reform.math.Vector;

public class OffsetCenterPoint implements ReferencePoint
{

	private final ReferencePoint _refA;
	private final ReferencePoint _refB;
	private final Length _offset;

	public OffsetCenterPoint(final ReferencePoint refA, final ReferencePoint refB, final Length offset)
	{
		_refA = refA;
		_refB = refB;
		_offset = offset;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);
		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);
		final double offset = _offset.getValueForRuntime(runtime);

		final double deltaX = bX - aX;
		final double deltaY = bY - aY;
		final double length = Vector.length(deltaX, deltaY);
		final double cx = (aX + bX) / 2;

		if (length == 0)
		{
			return aX;
		}

		final double orthogonalX = Vector.orthogonalX(deltaX, deltaY) / length;

		return cx - orthogonalX * offset;
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);
		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);
		final double offset = _offset.getValueForRuntime(runtime);

		final double deltaX = bX - aX;
		final double deltaY = bY - aY;
		final double length = Vector.length(deltaX, deltaY);
		final double cy = (aY + bY) / 2;

		if (length == 0)
		{
			return aY;
		}

		final double orthogonalY = Vector.orthogonalY(deltaX, deltaY) / length;

		return cy - orthogonalY * offset;
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return "center of " + _refA.getDescription(analyzer) + " and " + _refB.getDescription(analyzer);
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return _refA.isValidFor(runtime) && _refB.isValidFor(runtime);
	}
}
