package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.TranslationDistance;
import reform.math.Vec2;

public class ConstantDistance implements TranslationDistance {

	private final Vec2 _delta = new Vec2();

	public ConstantDistance(final double x, final double y) {
		_delta.set(x, y);
	}

	public ConstantDistance(final Vec2 delta) {
		_delta.set(delta);
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime) {
		return _delta.x;
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime) {
		return _delta.y;
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		return deltaAsString();
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
		return true;
	}

	public Vec2 getDelta() {
		return _delta;
	}

	@Override
	public boolean isDegenerated() {
		return _delta.x == 0 && _delta.y == 0;
	}

	public void setDelta(final Vec2 delta) {
		_delta.set(delta);
	}
}
