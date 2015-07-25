package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ScaleFactor;

public class ConstantScaleFactor implements ScaleFactor
{

	private double _factor;

	public ConstantScaleFactor(final double factor)
	{
		_factor = factor;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime)
	{
		return _factor;
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return String.format("%.1f%%", _factor * 100);
	}

	@Override
	public boolean isDegenerated()
	{
		return _factor == 1;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return true;
	}

	public double getValue()
	{
		return _factor;
	}

	public void setFactor(final double factor)
	{
		_factor = factor;
	}

}
