package reform.core.runtime.errors;

import reform.core.runtime.relations.ScaleFactor;

public class InvalidFactorError implements RuntimeError
{
	private final ScaleFactor _factor;

	public InvalidFactorError(ScaleFactor factor)
	{
		_factor = factor;
	}

	public ScaleFactor getFactor() {
		return _factor;
	}
	@Override
	public String getMessage()
	{
		return "Invalid Factor";
	}
}
