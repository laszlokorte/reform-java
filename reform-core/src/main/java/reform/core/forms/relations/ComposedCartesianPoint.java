package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;
import reform.core.runtime.relations.ReferencePoint;

public class ComposedCartesianPoint implements ReferencePoint
{

	private final Length _x;
	private final Length _y;

	public ComposedCartesianPoint(final Length x, final Length y)
	{
		_x = x;
		_y = y;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return _x.getValueForRuntime(runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return _y.getValueForRuntime(runtime);
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
		return _x.isValidFor(runtime) && _y.isValidFor(runtime);
	}

}
