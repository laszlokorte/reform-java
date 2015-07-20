package reform.core.forms.outline;

import reform.core.runtime.Runtime;

public interface Outline
{
	double getXForRuntime(Runtime runtime, double t);

	double getYForRuntime(Runtime runtime, double t);

	double getLengthForRuntime(Runtime runtime);
}
