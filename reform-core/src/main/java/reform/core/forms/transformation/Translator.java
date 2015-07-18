package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public interface Translator {
	void translate(Runtime runtime, double deltaX, double deltaY);
}
