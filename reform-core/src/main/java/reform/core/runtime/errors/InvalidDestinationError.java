package reform.core.runtime.errors;

import reform.core.runtime.relations.InitialDestination;

public class InvalidDestinationError implements RuntimeError
{
	private final InitialDestination _destination;

	public InvalidDestinationError(final InitialDestination destination)
	{
		_destination = destination;
	}

	public InitialDestination getDestination()
	{
		return _destination;
	}

	@Override
	public String getMessage()
	{
		return "Invalid Destination";
	}
}
