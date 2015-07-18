package reform.core.forms.relations;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Direction;

public class ProportionalDirection implements Direction {

	private final double _proportion;

	public ProportionalDirection(final double proportion) {
		_proportion = proportion;
	}

	@Override
	public double getAdjustedXForRuntime(final Runtime runtime,
			final double anchorX, final double anchorY, final double x,
			final double y) {

		final double dx = x - anchorX;
		final double dy = y - anchorY;

		return anchorX + getProportionedX(dx, dy);
	}

	@Override
	public double getAdjustedYForRuntime(final Runtime runtime,
			final double anchorX, final double anchorY, final double x,
			final double y) {
		final double dx = x - anchorX;
		final double dy = y - anchorY;

		return anchorY + getProportionedY(dx, dy);
	}

	public double getProportionedX(final double dx, final double dy) {
		final double min = Math.min(Math.abs(dx), Math.abs(dy / _proportion));

		return Math.signum(dx) * min;
	}

	public double getProportionedY(final double dx, final double dy) {
		final double min = Math.min(Math.abs(dx), Math.abs(dy / _proportion));

		return Math.signum(dy) * min * _proportion;
	}

	public double getProportion() {
		return _proportion;
	}

}