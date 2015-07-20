package reform.core.attributes;


import reform.identity.FastIterable;

public class AttributeSet implements FastIterable<Attribute<?>>
{
	private final Attribute<?>[] _attributes;

	public AttributeSet(final Attribute<?>... attributes)
	{
		_attributes = attributes;
	}

	public int size()
	{
		return _attributes.length;
	}

	public Attribute<?> get(final int i)
	{
		return _attributes[i];
	}
}
