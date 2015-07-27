package reform.core.attributes;

import reform.core.runtime.*;
import reform.identity.Identifier;

public class ConstantIdValue<T> implements IdValue<T>
{
	private Identifier<T> _identifier;

	@Override
	public Identifier<T> getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _identifier;
	}

	public Identifier<T> getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(Identifier<T> id) {
		_identifier = id;
	}
}
