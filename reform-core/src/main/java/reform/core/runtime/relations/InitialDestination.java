package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface InitialDestination extends Validatable {
	public static enum Alignment {
		Leading {
			@Override
			public double getAlignedMax(final double a, final double b) {
				return b;
			}

			@Override
			public double getAlignedMin(final double a, final double b) {
				return a;
			}
		},
		Center {
			@Override
			public double getAlignedMax(final double a, final double b) {
				return b;
			}

			@Override
			public double getAlignedMin(final double a, final double b) {
				return a - (b - a);
			}
		};

		abstract public double getAlignedMin(double a, double b);

		abstract public double getAlignedMax(double a, double b);
	}

	public double getMinXForRuntime(final Runtime runtime);

	public double getMinYForRuntime(final Runtime runtime);

	public double getMaxXForRuntime(final Runtime runtime);

	public double getMaxYForRuntime(final Runtime runtime);

	public Alignment getAlignment();

	public String getDescription(Analyzer analyzer);

	public boolean isDegenerated();

	public void setAlignment(final Alignment alignment);
}
