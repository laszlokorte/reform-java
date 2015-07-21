package reform.core.runtime.errors;


public class UnexpectedInternalError implements RuntimeError
{

	private final RuntimeException _exception;

	public UnexpectedInternalError(final RuntimeException exception) {
		_exception = exception;
	}

	@Override
	public String getMessage()
	{
		return _exception.getClass().getSimpleName();
	}
}
