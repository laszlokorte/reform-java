package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;

public class CenterPoint implements ReferencePoint
{

	private final ReferencePoint _refA;
	private final ReferencePoint _refB;

	public CenterPoint(final ReferencePoint refA, final ReferencePoint refB)
	{
		_refA = refA;
		_refB = refB;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return (_refA.getXValueForRuntime(runtime) + _refB.getXValueForRuntime(runtime)) / 2;
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return (_refA.getYValueForRuntime(runtime) + _refB.getYValueForRuntime(runtime)) / 2;

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
