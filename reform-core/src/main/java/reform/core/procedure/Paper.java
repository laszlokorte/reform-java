package reform.core.procedure;

import java.awt.geom.GeneralPath;

import reform.core.forms.Form;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.CenterPoint;
import reform.core.forms.relations.ComposedCartesianPoint;
import reform.core.forms.relations.ConstantLength;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.ScaledLength;
import reform.core.forms.relations.StaticLength;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;
import reform.naming.Name;

public class Paper implements Form {

	public static final int ID = 0;

	private final Identifier<Paper> _id = new Identifier<>(ID);
	private final Name _name = new Name("Canvas");

	private final IdentifiableList<ExposedPoint> _snapPoints = new IdentifiableList<>();
	private final IdentifiableList<Anchor> _anchors = new IdentifiableList<>();

	private final StaticLength _width = new StaticLength(getId(), 0);
	private final StaticLength _height = new StaticLength(getId(), 1);

	private final Outline _outline = new NullOutline();

	public static enum Point implements ExposedPointToken<Paper> {
		Center(0), TopRight(1), BottomRight(2), TopLeft(3), BottomLeft(4), Top(
				5), Right(6), Bottom(7), Left(8);

		private final int _v;

		Point(final int i) {
			_v = i;
		}

		@Override
		public int getValue() {
			return _v;
		}
	}

	public Paper() {
		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
						new ConstantLength(0)),
				new Name("Top Left"), Point.TopLeft));
		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(_width, new ConstantLength(0)),
				new Name("Top Right"), Point.TopRight));
		_snapPoints.add(
				new ExposedPoint(new ComposedCartesianPoint(_width, _height),
						new Name("Bottom Right"), Point.BottomRight));
		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(new ConstantLength(0), _height),
				new Name("Bottom Left"), Point.BottomLeft));

		_snapPoints.add(new ExposedPoint(
				new CenterPoint(
						new ComposedCartesianPoint(new ConstantLength(0),
								new ConstantLength(0)),
						new ComposedCartesianPoint(_width, _height)),
				new Name("Center"), Point.Center));

		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(new ScaledLength(_width, 0.5),
						new ConstantLength(0)),
				new Name("Top"), Point.Top));

		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
						new ScaledLength(_height, 0.5)),
				new Name("Right"), Point.Right));

		_snapPoints
				.add(new ExposedPoint(
						new ComposedCartesianPoint(
								new ScaledLength(_width, 0.5), _height),
						new Name("Bottom"), Point.Bottom));

		_snapPoints.add(new ExposedPoint(
				new ComposedCartesianPoint(_width,
						new ScaledLength(_height, 0.5)),
				new Name("Left"), Point.Left));
	}

	@Override
	public Identifier<Paper> getId() {
		return _id;
	}

	@Override
	public int getSizeOnStack() {
		return 2;
	}

	@Override
	public void initialize(final Runtime runtime, final double minX,
			final double minY, final double maxX, final double maxY) {
		_width.setForRuntime(runtime, maxX - minX);
		_height.setForRuntime(runtime, maxY - minY);
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime,
			final GeneralPath.Double reuse) {
	}

	@Override
	public ReferencePoint getPoint(
			final Identifier<? extends ExposedPoint> pointId) {
		return _snapPoints.getById(pointId);
	}

	@Override
	public Name getName() {
		return _name;
	}

	@Override
	public Rotator getRotator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Scaler getScaler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Translator getTranslator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Identifier<? extends ExposedPoint>> getPoints() {
		return _snapPoints;
	}

	@Override
	public DrawingType getType() {
		return DrawingType.None;
	}

	@Override
	public Iterable<Identifier<? extends Anchor>> getAnchors() {
		return _anchors;
	}

	@Override
	public Anchor getAnchor(final Identifier<? extends Anchor> anchorId) {
		return _anchors.getById(anchorId);
	}

	@Override
	public Outline getOutline() {
		return _outline;
	}

	@Override
	public void setType(final DrawingType draw) {
		throw new Error("Can not set drawing type of paper");
	}

}
