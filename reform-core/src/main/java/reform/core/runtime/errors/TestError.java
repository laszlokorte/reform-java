package reform.core.runtime.errors;

public class TestError implements RuntimeError
{
	@Override
	public String getMessage()
	{
		return "Some artificial test error";
	}
}
