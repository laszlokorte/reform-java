package reform.stage.elements;

import reform.core.runtime.relations.ReferencePoint;

public interface SnapPoint {
	public double getX();

	public double getY();

	public ReferencePoint createReference();
}
