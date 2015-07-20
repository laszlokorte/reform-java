package reform.core.forms.relations;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;

public class ConstantLength implements Length
{

	private final double _length;

	public ConstantLength(final double length)
	{
		_length = length;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime)
	{
		return _length;
	}


	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return true;
	}
}
