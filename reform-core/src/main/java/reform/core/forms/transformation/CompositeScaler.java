package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public class CompositeScaler implements Scaler
{

	private final Scaler[] _scalers;

	public CompositeScaler(final Scaler... scalers)
	{
		_scalers = scalers;
	}

	@Override
	public void scale(final Runtime runtime, final double factor, final double fixX, final double fixY, final double
			directionX, final double directionY)
	{
		for (int i = 0; i < _scalers.length; i++)
		{
			_scalers[i].scale(runtime, factor, fixX, fixY, directionX, directionY);
		}
	}

}
