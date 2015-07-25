package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface InitialDestination extends Validatable
{
	double getMinXForRuntime(final Runtime runtime);

	double getMinYForRuntime(final Runtime runtime);

	double getMaxXForRuntime(final Runtime runtime);

	double getMaxYForRuntime(final Runtime runtime);

	Alignment getAlignment();

	void setAlignment(final Alignment alignment);

	String getDescription(Analyzer analyzer);

	boolean isDegenerated();

	enum Alignment
	{
		Leading
				{
					@Override
					public double getAlignedMax(final double a, final double b)
					{
						return b;
					}

					@Override
					public double getAlignedMin(final double a, final double b)
					{
						return a;
					}
				},
		Center
				{
					@Override
					public double getAlignedMax(final double a, final double b)
					{
						return b;
					}

					@Override
					public double getAlignedMin(final double a, final double b)
					{
						return a - (b - a);
					}
				};

		abstract public double getAlignedMin(double a, double b);

		abstract public double getAlignedMax(double a, double b);
	}
}
