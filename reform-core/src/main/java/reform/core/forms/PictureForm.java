package reform.core.forms;

import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.attributes.BooleanValue;
import reform.core.attributes.IdValue;
import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.*;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.transformation.*;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.project.Picture;
import reform.core.runtime.Evaluable;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.RuntimeError;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public final class PictureForm extends BaseForm<PictureForm>
{

	static private final Identifier<?extends  Picture> DEFAULT_PICTURE_ID =
			new Identifier<>(-1);
	static private final int SIZE = 5;

	private final transient StaticPoint _centerPoint = new StaticPoint(getId(), 0);
	private final transient StaticLength _width = new StaticLength(getId(), 2);
	private final transient StaticLength _height = new StaticLength(getId(), 3);
	private final transient StaticAngle _rotation = new StaticAngle(getId(), 4);

	private final Outline _outline = new NullOutline();

	private final Translator _translator = new BasicTranslator(_centerPoint);
	private final Rotator _rotator = new CompositeRotator(
			new BasicPointRotator(_centerPoint), new BasicAngleRotator(_rotation));
	private final Scaler _scaler = new CompositeScaler(new BasicPointScaler
			(_centerPoint),
			new BasicLengthScaler(_width, _rotation, 0),
			new BasicLengthScaler(_height, _rotation, Math.PI / 2));

	private final Attribute<IdValue<? extends Picture>> _pictureIdAttribute = new
			Attribute<>(
			"Picture", IdValue.class, new IdValue<>(DEFAULT_PICTURE_ID));
	private final Attribute<BooleanValue> _proportionallyAttribute = new
			Attribute<>(
			"Proportionally", BooleanValue.class, new BooleanValue(true));

	private final AttributeSet _attributes = new AttributeSet(_proportionallyAttribute, _pictureIdAttribute);


	private final ColoredShape[] _shapes = new ColoredShape[Runtime.MAX_DEPTH];
	private final int[] _sizes = new int[Runtime.MAX_DEPTH * 2];

	{
		for (int i = 0; i < _shapes.length; i++)
		{
			_shapes[i] = new ColoredShape();
		}
	}

	private final Runtime.Listener _listener = new Runtime.Listener()
	{

		@Override
		public void onBeginEvaluation(final Runtime runtime)
		{
			int depth = runtime.getDepth();
			_sizes[2 * depth - 2] = runtime.getSize().x;
			_sizes[2 * depth - 1] = runtime.getSize().y;
		}

		@Override
		public void onFinishEvaluation(final Runtime runtime)
		{
			_shapes[runtime.getDepth()].reset();

		}

		@Override
		public void onEvalInstruction(final Runtime runtime, final Evaluable instruction)
		{

		}

		@Override
		public void onPopScope(final Runtime runtime, final FastIterable<Identifier<?
				extends Form>> ids)
		{
			int depth = runtime.getDepth();
			for (int i = 0, j = ids.size(); i < j; i++)
			{
				Form form = runtime.get(ids.get(i));
				if (form.getType() == DrawingType.Draw)
				{
					ColoredShape s = new ColoredShape();
					form.writeColoredShapeForRuntime(runtime, s);

					ColoredShape parentShape = _shapes[depth - 1];
					parentShape.addSubShape(s);
				}
			}

		}

		@Override
		public void onError(final Runtime runtime, final Evaluable instruction, final
		RuntimeError error)
		{

		}
	};

	private PictureForm(final Identifier<PictureForm> id, final Name name)
	{
		super(id, SIZE, name);

		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"), Point.Center));
		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Right.x * 0.5),
						new ScaledLength(_height, PictureAnchor.Side.Top.y * 0.5)),
				_rotation)), new Name("Top Right"), Point.TopRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Right.x * 0.5),
						new ScaledLength(_height, PictureAnchor.Side.Bottom.y * 0.5)),
				_rotation)), new Name("Bottom Right"), Point.BottomRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Left.x * 0.5),
						new ScaledLength(_height, PictureAnchor.Side.Bottom.y * 0.5)),
				_rotation)), new Name("Bottom Left"), Point.BottomLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Left.x * 0.5),
						new ScaledLength(_height, PictureAnchor.Side.Top.y * 0.5)),
				_rotation)), new Name("Top Left"), Point.TopLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Right.x * 0.5),
						new ConstantLength(0)), _rotation)), new Name("Right"),
				Point.Right));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
						new ScaledLength(_height, PictureAnchor.Side.Bottom.y * 0.5)),
				_rotation)), new Name("Bottom"), Point.Bottom));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(
						new ScaledLength(_width, PictureAnchor.Side.Left.x * 0.5),
						new ConstantLength(0)), _rotation)), new Name("Left"),
				Point.Left));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(
				new ComposedCartesianPoint(new ConstantLength(0),
						new ScaledLength(_height, PictureAnchor.Side.Top.y * 0.5)),
				_rotation)), new Name("Top"), Point.Top));

		addAnchor(new PictureAnchor(Anchor.TopLeft, new Name("Top Left"), _centerPoint,
				_rotation, _width, _height, PictureAnchor.Side.TopLeft));
		addAnchor(new PictureAnchor(Anchor.TopRight, new Name("Top Right"), _centerPoint,
				_rotation, _width, _height, PictureAnchor.Side.TopRight));
		addAnchor(new PictureAnchor(Anchor.BottomRight, new Name("Bottom Right"),
				_centerPoint, _rotation, _width, _height, PictureAnchor.Side
				.BottomRight));
		addAnchor(new PictureAnchor(Anchor.BottomLeft, new Name("Bottom Left"),
				_centerPoint, _rotation, _width, _height, PictureAnchor.Side
				.BottomLeft));

		addAnchor(
				new PictureAnchor(Anchor.Top, new Name("Center"), _centerPoint,
						_rotation,
						_width, _height, PictureAnchor.Side.Top));
		addAnchor(new PictureAnchor(Anchor.Right, new Name("Right"), _centerPoint,
				_rotation, _width, _height, PictureAnchor.Side.Right));
		addAnchor(new PictureAnchor(Anchor.Bottom, new Name("Bottom"), _centerPoint,
				_rotation, _width, _height, PictureAnchor.Side.Bottom));
		addAnchor(
				new PictureAnchor(Anchor.Left, new Name("Left"), _centerPoint, _rotation,
						_width, _height, PictureAnchor.Side.Left));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX, final double minY,
	                       final double maxX, final double maxY)
	{
		double width = Math.abs(maxX - minX);
		double height = Math.abs(maxY - minY);
		_rotation.setForRuntime(runtime, 0);
		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2, (minY + maxY) / 2);
		_width.setForRuntime(runtime, maxX - minX);
		_height.setForRuntime(runtime, maxY - minY);
		int depth = runtime.getDepth();

		Identifier<? extends Picture> pictureId = _pictureIdAttribute.getValue()
				.getValueForRuntime(runtime);

		boolean keepProportion = _proportionallyAttribute.getValue().getValueForRuntime(
				runtime);
		Picture p = runtime.subCall(pictureId, (int) (width), (int) (height), keepProportion);
		if (p != null)
		{
			_shapes[depth].reset();
			runtime.addListener(_listener);
			p.getProcedure().evaluate(runtime);
			runtime.subEnd();

		}
		else
		{
			_shapes[depth].reset();
			_sizes[2 * depth] = (int) width;
			_sizes[2 * depth + 1] = (int) height;
		}
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

		target.moveTo(5, 0);
		target.lineTo(width2 - 5, height2 - 10);
		target.lineTo(width2 - 5, -height2 + 10);
		target.closePath();

		target.moveTo(0, 5);
		target.lineTo(-width2 + 10, height2 - 5);
		target.lineTo(width2 - 10, height2 - 5);
		target.closePath();

		target.moveTo(0, -5);
		target.lineTo(width2 - 10, -height2 + 5);
		target.lineTo(-width2 + 10, -height2 + 5);
		target.closePath();


		target.moveTo(-5, 0);
		target.lineTo(-width2 + 5, -height2 + 10);
		target.lineTo(-width2 + 5, height2 - 10);
		target.closePath();

		target.transform(
				AffineTransform.getRotateInstance(_rotation.getValueForRuntime
						(runtime)));

		target.transform(AffineTransform.getTranslateInstance(x, y));
	}


	AffineTransform _t = new AffineTransform();

	@Override
	public void writeColoredShapeForRuntime(final Runtime runtime, final ColoredShape
			coloredShape)
	{

		int depth = runtime.getDepth();

		if (depth == Runtime.MAX_DEPTH)
		{
			return;
		}

		final double width2 = _width.getValueForRuntime(runtime) / 2;
		final double height2 = _height.getValueForRuntime(runtime) / 2;
		final double x = _centerPoint.getXValueForRuntime(runtime);
		final double y = _centerPoint.getYValueForRuntime(runtime);
		double rot = _rotation.getValueForRuntime(runtime);

		int d = depth;

		double origWidth2 = _sizes[2 * d] / 2.0;
		double origHeight2 = _sizes[2 * d + 1] / 2.0;
		if (origWidth2 > 10 && origHeight2 > 10)
		{
			double widthRatio = width2 / origWidth2;
			double heightRatio = height2 / origHeight2;

			double scaleFactor = Math.min(Math.abs(widthRatio), Math.abs(heightRatio));
			double scaleX = Math.signum(widthRatio) * scaleFactor;
			double scaleY = Math.signum(heightRatio) * scaleFactor;


			_t.setToIdentity();
			_t.translate(x, y);
			_t.rotate(rot);
			_t.translate(-origWidth2*scaleX, -origHeight2*scaleY);
			_t.scale(scaleX, scaleY);
			coloredShape.setChildTransform(_t);
			coloredShape.addSubShapesFrom(_shapes[d]);
		}
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

	private void refresh(final Runtime runtime)
	{

	}

	public void setPicture(final Identifier<?extends Picture> pictureId)
	{
		_pictureIdAttribute.setValue(new IdValue<>(pictureId));
	}

	public static PictureForm construct(final Identifier<PictureForm> id, final Name
			name)
	{
		return new PictureForm(id, name);
	}

	public enum Point implements ExposedPointToken<PictureForm>
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

	static class PictureAnchor extends BaseAnchor
	{

		private final StaticPoint _center;
		private final StaticAngle _rotation;
		private final StaticLength _width;
		private final StaticLength _height;
		private final Side _side;

		public PictureAnchor(final IdentityToken id, final Name name, final StaticPoint
				center, final StaticAngle rotation, final StaticLength width, final
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
					_side.y * oldHalfHeight, oldRotation);

			final double oldDeltaY = Vector.getRotatedY(_side.x * oldHalfWidth,
					_side.y * oldHalfHeight, oldRotation);

			final double oldX = oldCenterX + oldDeltaX;
			final double oldY = oldCenterY + oldDeltaY;

			final double oppX = oldCenterX - oldDeltaX;
			final double oppY = oldCenterY - oldDeltaY;

			final double newX = oldX + Vector.projectionX(deltaX, deltaY,
					_side.projectionMultiplier * oldDeltaX,
					_side.projectionMultiplier * oldDeltaY);
			final double newY = oldY + Vector.projectionY(deltaX, deltaY,
					_side.projectionMultiplier * oldDeltaX,
					_side.projectionMultiplier * oldDeltaY);

			final double newCenterX = (oppX + newX) / 2;
			final double newCenterY = (oppY + newY) / 2;

			final double newHalfWidth = Vector.getRotatedX(newX - newCenterX,
					newY - newCenterY, -oldRotation);
			final double newHalfHeight = Vector.getRotatedY(newX - newCenterX,
					newY - newCenterY, -oldRotation);

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

			final double centerY = _center.getXValueForRuntime(runtime);

			final double deltaY = Vector.getRotatedY(_side.x * halfWidth,
					_side.y * halfHeight, rotation);

			return centerY + deltaY;
		}

		enum Side
		{
			Top(0, -1), Right(1, 0), Bottom(0, 1), Left(-1, 0), TopLeft(-1, -1),
			TopRight(1, -1), BottomRight(1, 1),
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

	static class PictureTranslator implements Translator
	{

		private final PictureForm _form;
		private final Translator _otherTranslator;

		public PictureTranslator(final PictureForm form, final Translator translator)
		{
			_form = form;
			_otherTranslator = translator;
		}

		@Override
		public void translate(final Runtime runtime, final double deltaX, final double
				deltaY)
		{
			_otherTranslator.translate(runtime, deltaX, deltaY);
			_form.refresh(runtime);
		}

	}

	static class PictureRotator implements Rotator
	{

		private final PictureForm _form;
		private final Rotator _otherRotator;

		public PictureRotator(final PictureForm form, final Rotator rotator)
		{
			_form = form;
			_otherRotator = rotator;
		}

		@Override
		public void rotate(final Runtime runtime, final double angle, final double fixX,
		                   final double fixY)
		{
			_otherRotator.rotate(runtime, angle, fixX, fixY);
			_form.refresh(runtime);
		}

	}

	static class PictureScaler implements Scaler
	{

		private final PictureForm _form;
		private final Scaler _otherScaler;

		public PictureScaler(final PictureForm form, final Scaler scaler)
		{
			_form = form;
			_otherScaler = scaler;
		}

		@Override
		public void scale(final Runtime runtime, final double factor, final double fixX,
		                  final double fixY, final double directionX, final double
				                          directionY)
		{
			_otherScaler.scale(runtime, factor, fixX, fixY, directionX, directionY);
			_form.refresh(runtime);
		}

	}

}
