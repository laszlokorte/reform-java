package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public class AbsoluteScaler implements Scaler {

	private final Scaler _scaler;

	public AbsoluteScaler(final Scaler scaler) {
		_scaler = scaler;
	}

	@Override
	public void scale(final Runtime runtime, final double factor,
			final double fixX, final double fixY, final double directionX,
			final double directionY) {
		_scaler.scale(runtime, Math.abs(factor), fixX, fixY, directionX,
				directionY);
	}

}
