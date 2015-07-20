package reform.core.forms.outline;

import reform.core.runtime.Runtime;

public class NullOutline implements Outline
{

	@Override
	public double getXForRuntime(final Runtime runtime, final double t)
	{
		return 0;
	}

	@Override
	public double getYForRuntime(final Runtime runtime, final double t)
	{
		return 0;
	}

	@Override
	public double getLengthForRuntime(final Runtime runtime)
	{
		return 0;
	}

}
