package reform.core.forms;

import java.awt.geom.GeneralPath;

import reform.core.forms.anchors.Anchor;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.naming.Named;

public interface Form extends Identifiable<Form>, Named {
	public int getSizeOnStack();

	public void initialize(Runtime runtime, double minX, double minY,
			double maxX, double maxY);

	public void appendToPathForRuntime(Runtime runtime,
			GeneralPath.Double target);

	public Iterable<Identifier<? extends ExposedPoint>> getPoints();

	public ReferencePoint getPoint(Identifier<? extends ExposedPoint> _pointId);

	public Rotator getRotator();

	public Scaler getScaler();

	public Translator getTranslator();

	public DrawingType getType();

	public Iterable<Identifier<? extends Anchor>> getAnchors();

	public Anchor getAnchor(Identifier<? extends Anchor> anchorId);

	public Outline getOutline();

	public void setType(DrawingType draw);

}
