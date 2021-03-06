package reform.core.forms;

import reform.core.attributes.*;
import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.*;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.transformation.*;
import reform.core.graphics.ColoredShape;
import reform.core.runtime.Runtime;
import reform.data.sheet.DataSet;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public final class RectangleForm extends BaseForm<RectangleForm>
{

	static private final int SIZE = 5;
	private static final AffineTransform _transform = new AffineTransform();
	private final transient StaticPoint _centerPoint = new StaticPoint(getId(), 0);
	private final transient StaticLength _width = new StaticLength(getId(), 2);
	private final transient StaticLength _height = new StaticLength(getId(), 3);
	private final transient StaticAngle _rotation = new StaticAngle(getId(), 4);
	private final Outline _outline = new NullOutline();

	private final Attribute<ColorValue> _fillColorAttribute = new Attribute<>("Fill Color",
			ColorValue.class, new ConstantColorValue(DEFAULT_FILL_COLOR));
	private final Attribute<ColorValue> _strokeColorAttribute = new Attribute<>("Stroke Color",
			ColorValue.class, new ConstantColorValue(DEFAULT_STROKE_COLOR));

	private final Attribute<NumberValue> _strokeWidthAttribute = new Attribute<>("Stroke Width",
			NumberValue.class, new NumberValue(DEFAULT_STROKE_WIDTH));


	private final AttributeSet _attributes = new AttributeSet(_fillColorAttribute,
	                                                          _strokeColorAttribute,
	                                                          _strokeWidthAttribute);
	private final Translator _translator = new BasicTranslator(_centerPoint);
	private final Rotator _rotator = new CompositeRotator(
			new BasicPointRotator(_centerPoint), new BasicAngleRotator(_rotation));
	private final Scaler _scaler = new CompositeScaler(new BasicPointScaler
			                                                   (_centerPoint), new
			BasicLengthScaler(_width,
	                                                                         _rotation,
	                                                                         0),
	                                                   new BasicLengthScaler(_height,
	                                                                         _rotation,
	                                                                         Math.PI /
			                                                                         2));

	private RectangleForm(final Identifier<RectangleForm> id, final Name name)
	{
		super(id, SIZE, name);

		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"), Point.Center));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Right.x * 0.5),
						new ScaledLength(_height, RectangleAnchor.Side.Top.y * 0.5)),
				_rotation)), new Name("Top Right"), Point.TopRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Right.x * 0.5),
						new ScaledLength(_height, RectangleAnchor.Side.Bottom.y * 0.5)),
				_rotation)), new Name("Bottom Right"), Point.BottomRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Left.x * 0.5),
						new ScaledLength(_height, RectangleAnchor.Side.Bottom.y * 0.5)),
				_rotation)), new Name("Bottom Left"), Point.BottomLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Left.x * 0.5),
						new ScaledLength(_height, RectangleAnchor.Side.Top.y * 0.5)),
				_rotation)), new Name("Top Left"), Point.TopLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Right.x * 0.5),
						new ConstantLength(0)), _rotation)), new Name("Right"),
		                              Point.Right));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
				                           new ScaledLength(_height,
				                                            RectangleAnchor.Side.Bottom
						                                            .y * 0.5)),
				_rotation)), new Name("Bottom"), Point.Bottom));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, RectangleAnchor.Side.Left.x * 0.5),
						new ConstantLength(0)), _rotation)), new Name("Left"),
		                              Point.Left));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
				                           new ScaledLength(_height,
				                                            RectangleAnchor.Side.Top.y *
						                                            0.5)),
				_rotation)), new Name("Top"), Point.Top));

		addAnchor(new RectangleAnchor(Anchor.TopLeft, new Name("Top Left"), _centerPoint,
		                              _rotation, _width, _height,
		                              RectangleAnchor.Side.TopLeft));
		addAnchor(
				new RectangleAnchor(Anchor.TopRight, new Name("Top Right"), _centerPoint,
				                    _rotation, _width, _height,
				                    RectangleAnchor.Side.TopRight));
		addAnchor(new RectangleAnchor(Anchor.BottomRight, new Name("Bottom Right"),
		                              _centerPoint, _rotation, _width, _height,
		                              RectangleAnchor.Side.BottomRight));
		addAnchor(new RectangleAnchor(Anchor.BottomLeft, new Name("Bottom Left"),
		                              _centerPoint, _rotation, _width, _height,
		                              RectangleAnchor.Side.BottomLeft));

		addAnchor(
				new RectangleAnchor(Anchor.Top, new Name("Top"), _centerPoint, _rotation,
				                    _width, _height, RectangleAnchor.Side.Top));
		addAnchor(new RectangleAnchor(Anchor.Right, new Name("Right"), _centerPoint,
		                              _rotation, _width, _height,
		                              RectangleAnchor.Side.Right));
		addAnchor(new RectangleAnchor(Anchor.Bottom, new Name("Bottom"), _centerPoint,
		                              _rotation, _width, _height,
		                              RectangleAnchor.Side.Bottom));
		addAnchor(new RectangleAnchor(Anchor.Left, new Name("Left"), _centerPoint,
		                              _rotation, _width, _height,
		                              RectangleAnchor.Side.Left));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX, final double minY,
	                       final double maxX, final double maxY)
	{

		_rotation.setForRuntime(runtime, 0);
		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2, (minY + maxY) / 2);
		_width.setForRuntime(runtime, Math.abs(maxX - minX));
		_height.setForRuntime(runtime, Math.abs(maxY - minY));
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime, final GeneralPath.Double
			target)
	{
		final double width2 = _width.getValueForRuntime(runtime) / 2;
		final double height2 = _height.getValueForRuntime(runtime) / 2;
		final double x = _centerPoint.getXValueForRuntime(runtime);
		final double y = _centerPoint.getYValueForRuntime(runtime);

		target.moveTo(-width2, -height2);
		target.lineTo(width2, -height2);
		target.lineTo(width2, height2);
		target.lineTo(-width2, height2);
		target.closePath();

		_transform.setToTranslation(x, y);
		_transform.rotate(_rotation.getValueForRuntime(runtime));

		target.transform(_transform);
	}

	@Override
	public void writeColoredShapeForRuntime(final Runtime runtime, final ColoredShape
			coloredShape)
	{
		final DataSet dataSet = runtime.getDataSet();

		coloredShape.setBackgroundColor(
				_fillColorAttribute.getValue().getValueForRuntime(runtime));
		coloredShape.setStrokeColor(
				_strokeColorAttribute.getValue().getValueForRuntime(runtime));
		coloredShape.setStrokeWidth(
				_strokeWidthAttribute.getValue().getValueForRuntime(runtime));

		appendToPathForRuntime(runtime, coloredShape.getPath());
	}

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

	@Override
	public AttributeSet getAttributes()
	{
		return _attributes;
	}

	public static RectangleForm construct(final Identifier<RectangleForm> id, final Name
			name)
	{
		return new RectangleForm(id, name);
	}

	public enum Point implements ExposedPointToken<RectangleForm>
	{
		Center(0), TopRight(1), BottomRight(2), TopLeft(3), BottomLeft(4), Top(5), Right(
			6), Bottom(7), Left(8);

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
		TopRight(1), BottomRight(2), TopLeft(3), BottomLeft(4), Top(5), Right(6), Bottom(
			7), Left(8);

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

	static class RectangleAnchor extends BaseAnchor
	{

		private final StaticPoint _center;
		private final StaticAngle _rotation;
		private final StaticLength _width;
		private final StaticLength _height;
		private final Side _side;
		public RectangleAnchor(final IdentityToken id, final Name name, final
		StaticPoint center, final StaticAngle rotation, final StaticLength width, final
		StaticLength height, final Side side)
		{
			super(id, name);
			_center = center;
			_rotation = rotation;
			_width = width;
			_height = height;
			_side = side;
		}

		@Override
		public void translate(final Runtime runtime, final double deltaX, final double
				deltaY)
		{
			final double oldRotation = _rotation.getValueForRuntime(runtime);

			final double oldHalfWidth = _width.getValueForRuntime(runtime) / 2;
			final double oldHalfHeight = _height.getValueForRuntime(runtime) / 2;

			final double oldCenterX = _center.getXValueForRuntime(runtime);
			final double oldCenterY = _center.getYValueForRuntime(runtime);

			final double oldDeltaX = Vector.getRotatedX(_side.x * oldHalfWidth,
			                                            _side.y * oldHalfHeight,
			                                            oldRotation);

			final double oldDeltaY = Vector.getRotatedY(_side.x * oldHalfWidth,
			                                            _side.y * oldHalfHeight,
			                                            oldRotation);

			final double oldX = oldCenterX + oldDeltaX;
			final double oldY = oldCenterY + oldDeltaY;

			final double oppX = oldCenterX - oldDeltaX;
			final double oppY = oldCenterY - oldDeltaY;

			final double newX = oldX + Vector.projectionX(deltaX, deltaY,
			                                              _side.projectionMultiplier *
					                                              oldDeltaX,
			                                              _side.projectionMultiplier *
					                                              oldDeltaY);
			final double newY = oldY + Vector.projectionY(deltaX, deltaY,
			                                              _side.projectionMultiplier *
					                                              oldDeltaX,
			                                              _side.projectionMultiplier *
					                                              oldDeltaY);

			final double newCenterX = (oppX + newX) / 2;
			final double newCenterY = (oppY + newY) / 2;

			final double newHalfWidth = Vector.getRotatedX(newX - newCenterX,
			                                               newY - newCenterY,
			                                               -oldRotation);
			final double newHalfHeight = Vector.getRotatedY(newX - newCenterX,
			                                                newY - newCenterY,
			                                                -oldRotation);

			final double newWidth = 2 * (oldHalfWidth + _side.x * (newHalfWidth - _side
					.x * oldHalfWidth));

			final double newHeight = 2 * (oldHalfHeight + _side.y * (newHalfHeight -
					_side.y * oldHalfHeight));

			_width.setForRuntime(runtime, newWidth);
			_height.setForRuntime(runtime, newHeight);

			_center.setForRuntime(runtime, newCenterX, newCenterY);
		}

		@Override
		public double getXValueForRuntime(final Runtime runtime)
		{
			final double rotation = _rotation.getValueForRuntime(runtime);

			final double halfWidth = _width.getValueForRuntime(runtime) / 2;
			final double halfHeight = _height.getValueForRuntime(runtime) / 2;

			final double centerX = _center.getXValueForRuntime(runtime);

			final double deltaX = Vector.getRotatedX(_side.x * halfWidth,
			                                         _side.y * halfHeight, rotation);

			return centerX + deltaX;
		}

		@Override
		public double getYValueForRuntime(final Runtime runtime)
		{
			final double rotation = _rotation.getValueForRuntime(runtime);

			final double halfWidth = _width.getValueForRuntime(runtime) / 2;
			final double halfHeight = _height.getValueForRuntime(runtime) / 2;

			final double centerY = _center.getYValueForRuntime(runtime);

			final double deltaY = Vector.getRotatedY(_side.x * halfWidth,
			                                         _side.y * halfHeight, rotation);

			return centerY + deltaY;
		}

		enum Side
		{
			Top(0, -1), Right(1, 0), Bottom(0, 1), Left(-1, 0), TopLeft(-1, -1),
			TopRight(
				1, -1), BottomRight(1, 1),
			BottomLeft(-1, 1);

			final int x;
			final int y;
			final int projectionMultiplier;

			Side(final int x, final int y)
			{
				this.x = x;
				this.y = y;
				this.projectionMultiplier = x == 0 || y == 0 ? 1 : 0;
			}
		}
	}
}
