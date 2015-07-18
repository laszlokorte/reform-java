package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public interface Translator {
	public void translate(Runtime runtime, double deltaX, double deltaY);
}
