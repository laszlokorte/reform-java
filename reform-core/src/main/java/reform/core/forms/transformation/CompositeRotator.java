package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public class CompositeRotator implements Rotator
{

	private final Rotator[] _rotators;

	public CompositeRotator(final Rotator... rotators)
	{
		_rotators = rotators;
	}

	@Override
	public void rotate(final Runtime runtime, final double angle, final double fixX, final double fixY)
	{
		for (int i = 0; i < _rotators.length; i++)
		{
			_rotators[i].rotate(runtime, angle, fixX, fixY);
		}
	}

}
