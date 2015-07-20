package reform.core.forms;

import reform.core.attributes.AttributeSet;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.graphics.Color;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.naming.Named;

import java.awt.geom.GeneralPath;

public interface Form extends Identifiable<Form>, Named
{
	Color DEFAULT_FILL = new Color(0x99888888);
	Color DEFAULT_STROKE = new Color(0xff333333);

	int getSizeOnStack();

	void initialize(Runtime runtime, double minX, double minY, double maxX, double maxY);

	void appendToPathForRuntime(Runtime runtime, GeneralPath.Double target);

	Iterable<Identifier<? extends ExposedPoint>> getPoints();

	ReferencePoint getPoint(Identifier<? extends ExposedPoint> _pointId);

	Rotator getRotator();

	Scaler getScaler();

	Translator getTranslator();

	DrawingType getType();

	Iterable<Identifier<? extends Anchor>> getAnchors();

	Anchor getAnchor(Identifier<? extends Anchor> anchorId);

	Outline getOutline();

	void setType(DrawingType draw);

	AttributeSet getAttributes();
}
