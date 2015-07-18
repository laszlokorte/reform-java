package reform.core.runtime.relations;

import reform.core.runtime.Runtime;

public interface Direction {
	public static enum CartesianDirection implements Direction {
		Vertical {
			@Override
			public double getAdjustedXForRuntime(final Runtime runtime,
					final double anchorX, final double anchorY, final double x,
					final double y) {
				return anchorX;
			}

			@Override
			public double getAdjustedYForRuntime(final Runtime runtime,
					final double anchorX, final double anchorY, final double x,
					final double y) {
				return y;
			}
		},
		Horizontal {
			@Override
			public double getAdjustedXForRuntime(final Runtime runtime,
					final double anchorX, final double anchorY, final double x,
					final double y) {
				return x;
			}

			@Override
			public double getAdjustedYForRuntime(final Runtime runtime,
					final double anchorX, final double anchorY, final double x,
					final double y) {
				return anchorY;
			}
		}
	}

	public double getAdjustedXForRuntime(Runtime runtime, double anchorX,
			double anchorY, double x, double y);

	public double getAdjustedYForRuntime(Runtime runtime, double anchorX,
			double anchorY, double x, double y);

}
