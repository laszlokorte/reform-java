package reform.core.runtime.errors;

import reform.core.runtime.relations.ReferencePoint;

public class InvalidFixpointError implements RuntimeError
{
	private final ReferencePoint _point;

	public InvalidFixpointError(final ReferencePoint point)
	{
		_point = point;
	}

	public ReferencePoint getPoint()
	{
		return _point;
	}

	@Override
	public String getMessage()
	{
		return "Invalid Fixpoint";
	}
}
