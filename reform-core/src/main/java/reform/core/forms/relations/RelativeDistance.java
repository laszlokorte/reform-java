package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Direction;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.TranslationDistance;

public class RelativeDistance implements TranslationDistance
{

	private final ReferencePoint _refA;
	private ReferencePoint _refB;
	private Direction _direction = FreeDirection.Free;

	public RelativeDistance(final ReferencePoint refA, final ReferencePoint refB)
	{
		_refA = refA;
		_refB = refB;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		final double deltaX = _refB.getXValueForRuntime(runtime) - _refA.getXValueForRuntime(runtime);
		final double deltaY = _refB.getYValueForRuntime(runtime) - _refA.getYValueForRuntime(runtime);

		return _direction.getAdjustedXForRuntime(runtime, 0, 0, deltaX, deltaY);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		final double deltaX = _refB.getXValueForRuntime(runtime) - _refA.getXValueForRuntime(runtime);
		final double deltaY = _refB.getYValueForRuntime(runtime) - _refA.getYValueForRuntime(runtime);

		return _direction.getAdjustedYForRuntime(runtime, 0, 0, deltaX, deltaY);
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return getDirectionString() + " so " + _refA.getDescription(analyzer) + " matches " + _refB.getDescription(
				analyzer);
	}

	private String getDirectionString()
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

	public Direction getDirection()
	{
		return _direction;
	}

	public ReferencePoint getReferenceA()
	{
		return _refA;
	}

	public ReferencePoint getReferenceB()
	{
		return _refB;
	}

	@Override
	public boolean isDegenerated()
	{
		return _refA.equals(_refB);
	}

	public void setDirection(final Direction direction)
	{
		_direction = direction;
	}

	public void setReferenceB(final ReferencePoint refB)
	{
		_refB = refB;
	}

}
