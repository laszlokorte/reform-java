package reform.core.forms.outline;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.math.Vector;

public class LineOutline implements Outline {

	private final ReferencePoint _from;
	private final ReferencePoint _to;

	public LineOutline(final ReferencePoint from, final ReferencePoint to) {
		_from = from;
		_to = to;
	}

	@Override
	public double getXForRuntime(final Runtime runtime, final double t) {
		return t * _to.getXValueForRuntime(runtime) + (1 - t)
				* _from.getXValueForRuntime(runtime);
	}

	@Override
	public double getYForRuntime(final Runtime runtime, final double t) {
		return t * _to.getYValueForRuntime(runtime) + (1 - t)
				* _from.getYValueForRuntime(runtime);
	}

	@Override
	public double getLengthForRuntime(final Runtime runtime) {
		final double fromX = _from.getXValueForRuntime(runtime);
		final double fromY = _from.getYValueForRuntime(runtime);
		final double toX = _to.getXValueForRuntime(runtime);
		final double toY = _to.getYValueForRuntime(runtime);

		return Vector.distance(fromX, fromY, toX, toY);
	}

	double getFromXForRuntime(final Runtime runtime) {
		return _from.getXValueForRuntime(runtime);
	}

	double getFromYForRuntime(final Runtime runtime) {
		return _from.getYValueForRuntime(runtime);
	}

	double getToXForRuntime(final Runtime runtime) {
		return _to.getXValueForRuntime(runtime);
	}

	double getToYForRuntime(final Runtime runtime) {
		return _to.getYValueForRuntime(runtime);
	}

}
