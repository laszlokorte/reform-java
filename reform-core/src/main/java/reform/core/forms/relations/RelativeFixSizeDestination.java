package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.InitialDestination;
import reform.core.runtime.relations.ReferencePoint;
import reform.math.Vec2;

public class RelativeFixSizeDestination implements InitialDestination {

	private final ReferencePoint _ref;
	private final Vec2 _delta;
	private Alignment _alignment = Alignment.Leading;

	public RelativeFixSizeDestination(final ReferencePoint ref, final Vec2 delta) {
		_ref = ref;
		_delta = delta;
	}

	@Override
	public double getMinXForRuntime(final Runtime runtime) {
		final double aX = _ref.getXValueForRuntime(runtime);
		final double bX = aX + _delta.x;

		return _alignment.getAlignedMin(aX, bX);
	}

	@Override
	public double getMinYForRuntime(final Runtime runtime) {
		final double aY = _ref.getYValueForRuntime(runtime);
		final double bY = aY + _delta.y;

		return _alignment.getAlignedMin(aY, bY);
	}

	@Override
	public double getMaxXForRuntime(final Runtime runtime) {
		final double aX = _ref.getXValueForRuntime(runtime);

		final double bX = aX + _delta.x;

		return _alignment.getAlignedMax(aX, bX);
	}

	@Override
	public double getMaxYForRuntime(final Runtime runtime) {
		final double aY = _ref.getYValueForRuntime(runtime);

		final double bY = aY + _delta.y;

		return _alignment.getAlignedMax(aY, bY);
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		return (_alignment == Alignment.Leading ? "from " : "around ")
				+ _ref.getDescription(analyzer) + " " + deltaAsString();
	}

	private String deltaAsString() {
		if (_delta.y == 0 && _delta.x != 0) {
			return String.format("%.1f Horizontally", _delta.x);
		} else if (_delta.x == 0 && _delta.y != 0) {
			return String.format("%.1f Vertically", _delta.y);
		} else {
			return String.format("%.1f Horizontally, %.1f Vertically",
					_delta.x, _delta.y);
		}
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return _ref.isValidFor(runtime);
	}

	@Override
	public Alignment getAlignment() {
		return _alignment;
	}

	public ReferencePoint getReference() {
		return _ref;
	}

	public Vec2 getDelta() {
		return _delta;
	}

	@Override
	public boolean isDegenerated() {
		return _delta.x == 0 && _delta.y == 0;
	}

	@Override
	public void setAlignment(final Alignment alignment) {
		_alignment = alignment;
	}

	public void setDelta(final Vec2 delta) {
		_delta.set(delta);
	}
}
