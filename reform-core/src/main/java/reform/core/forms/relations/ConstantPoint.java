package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.math.Vec2;

public class ConstantPoint implements ReferencePoint {

	private final Vec2 _value = new Vec2();

	public ConstantPoint(final Vec2 pos) {
		_value.set(pos);
	}

	public ConstantPoint(final double x, final double y) {
		_value.set(x, y);
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime) {
		return _value.x;
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime) {
		return _value.y;
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		return "(" + _value.x + ", " + _value.y + ")";
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return true;
	}

	public Vec2 getValue() {
		return _value;
	}

}
