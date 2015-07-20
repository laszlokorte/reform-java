package reform.stage.elements;

import reform.core.runtime.relations.ReferencePoint;

public interface SnapPoint
{
	double SNAP_RADIUS = 10;

	double getX();

	double getY();

	ReferencePoint createReference();

	String getLabel();

	boolean isInSnapRadius(double x, double y);
}
