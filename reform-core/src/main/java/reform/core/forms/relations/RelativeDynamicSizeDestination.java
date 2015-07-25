package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Direction;
import reform.core.runtime.relations.InitialDestination;
import reform.core.runtime.relations.ReferencePoint;

public class RelativeDynamicSizeDestination implements InitialDestination
{

	private final ReferencePoint _refA;
	private ReferencePoint _refB;
	private Direction _direction = FreeDirection.Free;
	private Alignment _alignment = Alignment.Leading;

	public RelativeDynamicSizeDestination(final ReferencePoint refA, final
	ReferencePoint refB)
	{
		_refA = refA;
		_refB = refB;
	}

	@Override
	public double getMinXForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);

		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);

		final double forcedBX = _direction.getAdjustedXForRuntime(runtime, aX, aY, bX,
		                                                          bY);
		return _alignment.getAlignedMin(aX, forcedBX);
	}

	@Override
	public double getMinYForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);

		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);

		final double forcedBY = _direction.getAdjustedYForRuntime(runtime, aX, aY, bX,
		                                                          bY);
		return _alignment.getAlignedMin(aY, forcedBY);

	}

	@Override
	public double getMaxXForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);

		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);

		final double forcedBX = _direction.getAdjustedXForRuntime(runtime, aX, aY, bX,
		                                                          bY);
		return _alignment.getAlignedMax(aX, forcedBX);
	}

	@Override
	public double getMaxYForRuntime(final Runtime runtime)
	{
		final double aX = _refA.getXValueForRuntime(runtime);
		final double aY = _refA.getYValueForRuntime(runtime);

		final double bX = _refB.getXValueForRuntime(runtime);
		final double bY = _refB.getYValueForRuntime(runtime);

		final double forcedBY = _direction.getAdjustedYForRuntime(runtime, aX, aY, bX,
		                                                          bY);
		return _alignment.getAlignedMax(aY, forcedBY);
	}

	@Override
	public Alignment getAlignment()
	{
		return _alignment;
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return directionAsString() + (_alignment == Alignment.Leading ? "from " :
				"around ") + _refA.getDescription(
				analyzer) + " to " + _refB.getDescription(analyzer);
	}

	@Override
	public boolean isDegenerated()
	{
		return _refA.equals(_refB);
	}

	@Override
	public void setAlignment(final Alignment alignment)
	{
		_alignment = alignment;
	}

	private String directionAsString()
	{
		if (_direction == Direction.CartesianDirection.Horizontal)
		{
			return "horizontally ";
		}
		else if (_direction == Direction.CartesianDirection.Vertical)
		{
			return "vertically ";
		}
		else if (_direction instanceof ProportionalDirection)
		{
			return "proportionally ";
		}
		else
		{
			return "";
		}
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return _refA.isValidFor(runtime) && _refB.isValidFor(runtime);
	}

	public ReferencePoint getReferenceA()
	{
		return _refA;
	}

	public ReferencePoint getReferenceB()
	{
		return _refB;
	}

	public void setReferenceB(final ReferencePoint refB)
	{
		_refB = refB;
	}

	public Direction getDirection()
	{
		return _direction;
	}

	public void setDirection(final Direction direction)
	{
		_direction = direction;
	}
}
