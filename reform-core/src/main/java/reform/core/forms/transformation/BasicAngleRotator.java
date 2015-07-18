package reform.core.forms.transformation;

import reform.core.forms.relations.StaticAngle;
import reform.core.runtime.Runtime;

public class BasicAngleRotator implements Rotator {

	private final StaticAngle _angle;

	public BasicAngleRotator(final StaticAngle angle) {
		_angle = angle;
	}

	@Override
	public void rotate(final Runtime runtime, final double angle,
			final double fixX, final double fixY) {
		_angle.setForRuntime(runtime, _angle.getValueForRuntime(runtime)
				+ angle);

	}

}
