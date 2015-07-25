package reform.core.runtime.errors;

import reform.core.forms.anchors.Anchor;
import reform.identity.Identifier;

public class UnknownAnchorError implements RuntimeError
{
	private final Identifier<? extends Anchor> _anchorId;

	public UnknownAnchorError(final Identifier<? extends Anchor> anchorId)
	{
		_anchorId = anchorId;
	}

	public Identifier<? extends Anchor> getAnchorId()
	{
		return _anchorId;
	}

	@Override
	public String getMessage()
	{
		return "Invalid Anchor Point";
	}
}
