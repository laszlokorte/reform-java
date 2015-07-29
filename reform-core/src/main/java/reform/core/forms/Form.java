package reform.core.forms;

import reform.core.attributes.*;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.data.sheet.Value;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.naming.Named;

import java.awt.geom.GeneralPath;

public interface Form extends Identifiable<Form>, Named
{
	int DEFAULT_FILL_COLOR = 0x99888888;
	int DEFAULT_STROKE_COLOR = 0xff333333;
	double DEFAULT_STROKE_WIDTH = 1;

	int getSizeOnStack();

	void initialize(Runtime runtime, double minX, double minY, double maxX, double maxY);

	void appendToPathForRuntime(Runtime runtime, GeneralPath.Double target);

	void writeColoredShapeForRuntime(Runtime runtime, ColoredShape coloredShape);

	Iterable<Identifier<? extends ExposedPoint>> getPoints();

	ReferencePoint getPoint(Identifier<? extends ExposedPoint> _pointId);

	Rotator getRotator();

	Scaler getScaler();

	Translator getTranslator();

	DrawingType getType();

	void setType(DrawingType draw);

	Iterable<Identifier<? extends Anchor>> getAnchors();

	Anchor getAnchor(Identifier<? extends Anchor> anchorId);

	Outline getOutline();

	AttributeSet getAttributes();
}
