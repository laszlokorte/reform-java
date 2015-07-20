package reform.identity;

public class IdentifierEmitter
{
	private int _counter = 0;

	public IdentifierEmitter(final int initial)
	{
		_counter = initial;
	}

	public <T> Identifier<T> emit()
	{
		return new Identifier<>(_counter++);
	}

	public <T> void markUsed(final Identifier<T> identifier)
	{
		_counter = Math.max(Identifier.getValue(identifier) + 1, _counter);
	}
}
