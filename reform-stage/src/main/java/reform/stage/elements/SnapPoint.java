package reform.stage.elements;

import reform.core.runtime.relations.ReferencePoint;

public interface SnapPoint {
	double getX();

	double getY();

	ReferencePoint createReference();
}
