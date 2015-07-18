package reform.core.forms.anchors;

import reform.core.runtime.Runtime;
import reform.identity.Identifiable;
import reform.naming.Named;

public interface Anchor extends Identifiable<Anchor>, Named {
	void translate(Runtime runtime, final double x, final double y);
}
