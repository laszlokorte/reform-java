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
	private final byte _subscript;

	/**
	 * Create a new identifier from the given token.
	 */
	public Identifier(final IdentityToken token)
	{
		_value = token.getValue();
		_subscript = 0;
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
		_subscript = 0;
	}

	public Identifier(final int value, final byte subscript)
	{
		_value = value;
		_subscript = subscript;
	}

	/**
	 * Creates a copy of the given id.
	 *
	 * @param id
	 */
	public Identifier(final Identifier<? extends T> id)
	{
		_value = id._value;
		_subscript = id._subscript;
	}


	public Identifier(final Identifier<? extends T> id, final byte subscript)
	{
		_value = id._value;
		_subscript = subscript;
	}

	@Override
	public int hashCode()
	{
		return (_value << 8) | _subscript;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (obj == null)
		{
			return false;
		}
		else if (obj.getClass() != getClass())
		{
			return false;
		}

		@SuppressWarnings("unchecked") final Identifier<? extends T> other = (Identifier<? extends T>) obj;
		return other._value == _value && other._subscript == _subscript;
	}

	@Override
	public String toString()
	{
		return "" + _value + ":" + _subscript;
	}

	public static int getValue(final Identifier<?> id)
	{
		return id._value;
	}

	public static byte getSubscript(final Identifier<?> id)
	{
		return id._subscript;
	}
}
