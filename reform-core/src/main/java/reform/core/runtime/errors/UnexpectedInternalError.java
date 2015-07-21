package reform.core.runtime.errors;


public class UnexpectedInternalError implements RuntimeError
{

	private final Throwable _throwable;

	public UnexpectedInternalError(final Throwable throwable) {
		_throwable = throwable;
	}

	@Override
	public String getMessage()
	{
		return _throwable.getMessage();
	}
}
