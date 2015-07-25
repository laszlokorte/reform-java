package reform.core.forms;

import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.CircleOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.*;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.transformation.*;
import reform.core.graphics.ColoredShape;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.data.sheet.DataSet;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public final class CircleForm extends BaseForm<CircleForm>
{
	static private final int SIZE = 4;

	private final transient StaticPoint _centerPoint = new StaticPoint(getId(), 0);
	private final transient StaticLength _radius = new StaticLength(getId(), 2);
	private final transient StaticAngle _rotation = new StaticAngle(getId(), 3);

	private final transient Translator _translator = new BasicTranslator(_centerPoint);

	private final transient Rotator _rotator = new CompositeRotator(new BasicPointRotator(_centerPoint),
	                                                                new BasicAngleRotator(_rotation));

	private final transient Scaler _scaler = new CompositeScaler(new BasicPointScaler(_centerPoint),
	                                                             new BasicLengthScaler(_radius, _rotation, 0));

	private final Outline _outline = new CircleOutline(_centerPoint, _radius, _rotation);

	private final Attribute _fillColorAttribute = new Attribute("Fill Color", Attribute.Type.Color,
	                                                            DEFAULT_FILL_COLOR);
	private final Attribute _strokeColorAttribute = new Attribute("Stroke Color", Attribute.Type.Color,
	                                                              DEFAULT_STROKE_COLOR);

	private final Attribute _strokeWidthAttribute = new Attribute("Stroke Width", Attribute.Type.Number,
	                                                              DEFAULT_STROKE_WIDTH);

	private final AttributeSet _attributes = new AttributeSet(_fillColorAttribute, _strokeColorAttribute,
	                                                          _strokeWidthAttribute);

	public enum Point implements ExposedPointToken<CircleForm>
	{
		Center(0), Top(1), Right(2), Bottom(3), Left(4);

		private final int _v;

		Point(final int i)
		{
			_v = i;
		}

		@Override
		public int getValue()
		{
			return _v;
		}
	}

	public enum Anchor implements IdentityToken
	{
		Top(1), Right(2), Bottom(3), Left(4);

		private final int _v;

		Anchor(final int i)
		{
			_v = i;
		}

		@Override
		public int getValue()
		{
			return _v;
		}
	}

	public static CircleForm construct(final Identifier<CircleForm> id, final Name name)
	{
		return new CircleForm(id, name);
	}

	private CircleForm(final Identifier<CircleForm> id, final Name name)
	{
		super(id, SIZE, name);
		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"), Point.Center));
		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(_radius, new ConstantLength(0)), _rotation)), new Name("Right"), Point
				.Right));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0), _radius), _rotation)), new Name("Bottom"),
		                              Point.Bottom));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ScaledLength(_radius, -1), new ConstantLength(0)), _rotation)),
		                              new Name("Left"), Point.Left));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0), new ScaledLength(_radius, -1)), _rotation)),
		                              new Name("Top"), Point.Top));

		addAnchor(new CircleQuarterAnchor(Anchor.Top, new Name("Top"), CircleQuarterAnchor.Quarter.North, _radius,
		                                  _rotation, _centerPoint));

		addAnchor(new CircleQuarterAnchor(Anchor.Right, new Name("Right"), CircleQuarterAnchor.Quarter.East, _radius,
		                                  _rotation, _centerPoint));

		addAnchor(new CircleQuarterAnchor(Anchor.Bottom, new Name("Bottom"), CircleQuarterAnchor.Quarter.South,
		                                  _radius,
		                                  _rotation, _centerPoint));

		addAnchor(new CircleQuarterAnchor(Anchor.Left, new Name("Left"), CircleQuarterAnchor.Quarter.West, _radius,
		                                  _rotation, _centerPoint));

	}

	@Override
	public void initialize(final Runtime runtime, final double minX, final double minY, final double maxX, final
	double maxY)
	{
		final double dx = maxX - minX;
		final double dy = maxY - minY;

		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2, (minY + maxY) / 2);
		_radius.setForRuntime(runtime, Math.sqrt(dx * dx + dy * dy) / 2);
		_rotation.setForRuntime(runtime, Math.atan2(-dy, -dx));
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime, final GeneralPath.Double target)
	{
		final double radius = _radius.getValueForRuntime(runtime);
		final double cx = _centerPoint.getXValueForRuntime(runtime);
		final double cy = _centerPoint.getYValueForRuntime(runtime);

		_circle.setFrame(cx - radius, cy - radius, 2 * radius, 2 * radius);
		target.append(_circle, false);
	}

	@Override
	public void writeColoredShapeForRuntime(final Runtime runtime, final ColoredShape coloredShape)
	{
		final DataSet dataSet = runtime.getDataSet();

		coloredShape.setBackgroundColor(_fillColorAttribute.getValue().getValueFor(dataSet).getColor());
		coloredShape.setStrokeColor(_strokeColorAttribute.getValue().getValueFor(dataSet).getColor());
		coloredShape.setStrokeWidth(_strokeWidthAttribute.getValue().getValueFor(dataSet).getInteger());

		appendToPathForRuntime(runtime, coloredShape.getPath());
	}

	private final Ellipse2D.Double _circle = new Ellipse2D.Double();

	@Override
	public Rotator getRotator()
	{
		return _rotator;
	}

	@Override
	public Scaler getScaler()
	{
		return _scaler;
	}

	@Override
	public Translator getTranslator()
	{
		return _translator;
	}

	@Override
	public Outline getOutline()
	{
		return _outline;
	}

	static class CircleQuarterAnchor extends BaseAnchor
	{

		enum Quarter
		{
			North(-Math.PI / 2), East(0), South(-3 * Math.PI / 2), West(Math.PI);

			final double angle;

			Quarter(final double angle)
			{
				this.angle = angle;
			}
		}

		private final Quarter _quarter;
		private final StaticLength _radius;
		private final StaticAngle _angle;
		private final ReferencePoint _center;

		public CircleQuarterAnchor(final IdentityToken token, final Name name, final Quarter quarter, final
		StaticLength radius, final StaticAngle angle, final ReferencePoint center)
		{
			super(token, name);
			_quarter = quarter;
			_radius = radius;
			_angle = angle;
			_center = center;
		}

		@Override
		public void translate(final Runtime runtime, final double x, final double y)
		{
			final double oldAngle = _angle.getValueForRuntime(runtime);
			final double oldRadius = _radius.getValueForRuntime(runtime);

			final double oldDeltaX = Vector.getRotatedX(oldRadius, 0, oldAngle + _quarter.angle);
			final double oldDeltaY = Vector.getRotatedY(oldRadius, 0, oldAngle + _quarter.angle);

			final double newDeltaX = oldDeltaX + x;
			final double newDeltaY = oldDeltaY + y;

			final double newRadius = Vector.length(newDeltaX, newDeltaY);

			final double newAngle = Vector.angleOf(newDeltaX, newDeltaY) - _quarter.angle;

			_radius.setForRuntime(runtime, newRadius);
			_angle.setForRuntime(runtime, newAngle);
		}

		@Override
		public double getXValueForRuntime(final Runtime runtime)
		{
			final double angle = _angle.getValueForRuntime(runtime);
			final double radius = _radius.getValueForRuntime(runtime);

			final double deltaX = Vector.getRotatedX(radius, 0, angle + _quarter.angle);

			return _center.getXValueForRuntime(runtime) + deltaX;
		}

		@Override
		public double getYValueForRuntime(final Runtime runtime)
		{
			final double angle = _angle.getValueForRuntime(runtime);
			final double radius = _radius.getValueForRuntime(runtime);

			final double deltaY = Vector.getRotatedY(radius, 0, angle + _quarter.angle);

			return _center.getYValueForRuntime(runtime) + deltaY;
		}

	}

	@Override
	public AttributeSet getAttributes()
	{
		return _attributes;
	}

}
