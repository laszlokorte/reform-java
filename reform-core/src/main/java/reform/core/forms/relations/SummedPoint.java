package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;

public class SummedPoint implements ReferencePoint {

	private final ReferencePoint _refA;
	private final ReferencePoint _refB;

	public SummedPoint(final ReferencePoint refA, final ReferencePoint refB) {
		_refA = refA;
		_refB = refB;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime) {
		return _refA.getXValueForRuntime(runtime)
				+ _refB.getXValueForRuntime(runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime) {
		return _refA.getYValueForRuntime(runtime)
				+ _refB.getYValueForRuntime(runtime);

	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		return null;
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return _refA.isValidFor(runtime) && _refB.isValidFor(runtime);
	}
}
