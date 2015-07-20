package reform.core.forms.anchors;

import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.naming.Name;

public abstract class BaseAnchor implements Anchor
{

	private final Name _name;
	private final Identifier<? extends Anchor> _id;

	public BaseAnchor(final IdentityToken id, final Name name)
	{
		_id = new Identifier<>(id);
		_name = name;
	}

	@Override
	final public Identifier<? extends Anchor> getId()
	{
		return _id;
	}

	@Override
	final public Name getName()
	{
		return _name;
	}

}
