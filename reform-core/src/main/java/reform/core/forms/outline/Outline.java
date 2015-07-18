package reform.core.forms.outline;

import reform.core.runtime.Runtime;

public interface Outline {
	public double getXForRuntime(Runtime runtime, double t);

	public double getYForRuntime(Runtime runtime, double t);

	public double getLengthForRuntime(Runtime runtime);
}
