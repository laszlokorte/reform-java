package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.RotationAngle;

public class ConstantAngle implements RotationAngle {

	private double _angle;

	public ConstantAngle(final double angle) {
		_angle = angle;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime) {
		return _angle;
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return true;
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		return String.format("%.2f%%", 50 * _angle / Math.PI);
	}

	public double getValue() {
		return _angle;
	}

	public boolean isDegenerated() {
		return _angle == 0;
	}

	public void setAngle(final double angle) {
		_angle = angle;
	}
}
