package reform.identity;

/**
 * An Identifier can be used to store a virtual reference to an Object without
 * storing the object itself.
 * <p>
 * The object itself should be stored in another location (eg. in a HashMap) to
 * be able to access it via the Identifier.
 * <p>
 * Two Identifiers with the same internal value are the same.
 *
 * @param <T> The type of the object to be identified
 */
public final class Identifier<T>
{
	private final int _value;

	/**
	 * Create a new identifier from the given token.
	 */
	public Identifier(final IdentityToken token)
	{
		_value = token.getValue();
	}

	/**
	 * Create an identifier the the given value. Two Identifiers with the same
	 * value are the same.
	 *
	 * @param value
	 */
	public Identifier(final int value)
	{
		_value = value;
	}

	/**
	 * Creates a copy of the given id.
	 *
	 * @param id
	 */
	public Identifier(final Identifier<? extends T> id)
	{
		_value = id._value;
	}

	@Override
	public int hashCode()
	{
		return _value;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof Identifier)
		{
			@SuppressWarnings("unchecked") final Identifier<? extends T> other = (Identifier<? extends T>) obj;
			return other._value == _value;
		}
		else
		{
			return super.equals(obj);
		}
	}

	@Override
	public String toString()
	{
		return "" + _value;
	}

	public static int getValue(final Identifier<?> id)
	{
		return id._value;
	}
}
