package reform.core.runtime.errors;

import reform.core.runtime.relations.TranslationDistance;

public class InvalidDistanceError implements RuntimeError
{
	private final TranslationDistance _distance;

	public InvalidDistanceError(final TranslationDistance distance)
	{
		_distance = distance;
	}

	public TranslationDistance getDistance()
	{
		return _distance;
	}

	@Override
	public String getMessage()
	{
		return "Invalid Distance";
	}
}
