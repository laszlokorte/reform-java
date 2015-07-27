package reform.core.attributes;

import reform.core.runtime.*;
import reform.identity.Identifier;

public interface IdValue<T>
{
	Identifier<T> getValueForRuntime(reform.core.runtime.Runtime runtime);

}
