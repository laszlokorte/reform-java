package reform.core.forms.transformation;

import reform.core.runtime.Runtime;

public interface Scaler {
	void scale(Runtime runtime, double factor, double fixX, double fixY,
               double directionX, double directionY);

}
