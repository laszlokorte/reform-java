package reform.core.forms;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import reform.core.forms.RectangleForm.RectangleAnchor;
import reform.core.forms.RectangleForm.RectangleAnchor.Side;
import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.ComposedCartesianPoint;
import reform.core.forms.relations.ConstantLength;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.RotatedPoint;
import reform.core.forms.relations.ScaledLength;
import reform.core.forms.relations.StaticAngle;
import reform.core.forms.relations.StaticLength;
import reform.core.forms.relations.StaticPoint;
import reform.core.forms.relations.SummedPoint;
import reform.core.forms.transformation.BasicAngleRotator;
import reform.core.forms.transformation.BasicLengthScaler;
import reform.core.forms.transformation.BasicPointRotator;
import reform.core.forms.transformation.BasicPointScaler;
import reform.core.forms.transformation.BasicTranslator;
import reform.core.forms.transformation.CompositeRotator;
import reform.core.forms.transformation.CompositeScaler;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

public class PictureForm extends BaseForm<PictureForm> {

	static private final int SIZE = 5;

	private final transient StaticPoint _centerPoint = new StaticPoint(getId(),
			0);
	private final transient StaticLength _width = new StaticLength(getId(), 2);
	private final transient StaticLength _height = new StaticLength(getId(), 3);
	private final transient StaticAngle _rotation = new StaticAngle(getId(), 4);

	private final Outline _outline = new NullOutline();

	public static enum Point implements ExposedPointToken<RectangleForm> {
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

	public static enum Anchor implements IdentityToken {
		TopRight(1), BottomRight(2), TopLeft(3), BottomLeft(4), Top(5), Right(6), Bottom(
				7), Left(8);

		private final int _v;

		Anchor(final int i) {
			_v = i;
		}

		@Override
		public int getValue() {
			return _v;
		}
	}

	private final Translator _translator = new BasicTranslator(_centerPoint);

	private final Rotator _rotator = new CompositeRotator(
			new BasicPointRotator(_centerPoint), new BasicAngleRotator(
					_rotation));

	private final Scaler _scaler = new CompositeScaler(new BasicPointScaler(
			_centerPoint), new BasicLengthScaler(_width, _rotation, 0),
			new BasicLengthScaler(_height, _rotation, Math.PI / 2));

	public static PictureForm construct(final Identifier<PictureForm> id,
			final Name name) {
		return new PictureForm(id, name);
	}

	private PictureForm(final Identifier<PictureForm> id, final Name name) {
		super(id, SIZE, name);

		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"),
				Point.Center));
		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Right.x * 0.5), new ScaledLength(_height,
						Side.Top.y * 0.5)), _rotation)), new Name("Top Right"),
				Point.TopRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Right.x * 0.5), new ScaledLength(_height,
								Side.Bottom.y * 0.5)), _rotation)), new Name(
										"Bottom Right"), Point.BottomRight));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Left.x * 0.5), new ScaledLength(_height,
								Side.Bottom.y * 0.5)), _rotation)), new Name(
										"Bottom Left"), Point.BottomLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Left.x * 0.5), new ScaledLength(_height,
								Side.Top.y * 0.5)), _rotation)), new Name("Top Left"),
								Point.TopLeft));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Right.x * 0.5), new ConstantLength(0)),
						_rotation)), new Name("Right"), Point.Right));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ConstantLength(
						0), new ScaledLength(_height, Side.Bottom.y * 0.5)),
						_rotation)), new Name("Bottom"), Point.Bottom));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_width, Side.Left.x * 0.5), new ConstantLength(0)),
						_rotation)), new Name("Left"), Point.Left));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ConstantLength(
						0), new ScaledLength(_height, Side.Top.y * 0.5)),
						_rotation)), new Name("Top"), Point.Top));

		addAnchor(new RectangleAnchor(Anchor.TopLeft, new Name("Top Left"),
				_centerPoint, _rotation, _width, _height, Side.TopLeft));
		addAnchor(new RectangleAnchor(Anchor.TopRight, new Name("Top Right"),
				_centerPoint, _rotation, _width, _height, Side.TopRight));
		addAnchor(new RectangleAnchor(Anchor.BottomRight, new Name(
				"Bottom Right"), _centerPoint, _rotation, _width, _height,
				Side.BottomRight));
		addAnchor(new RectangleAnchor(Anchor.BottomLeft,
				new Name("Bottom Left"), _centerPoint, _rotation, _width,
				_height, Side.BottomLeft));

		addAnchor(new RectangleAnchor(Anchor.Top, new Name("Center"),
				_centerPoint, _rotation, _width, _height, Side.Top));
		addAnchor(new RectangleAnchor(Anchor.Right, new Name("Right"),
				_centerPoint, _rotation, _width, _height, Side.Right));
		addAnchor(new RectangleAnchor(Anchor.Bottom, new Name("Bottom"),
				_centerPoint, _rotation, _width, _height, Side.Bottom));
		addAnchor(new RectangleAnchor(Anchor.Left, new Name("Left"),
				_centerPoint, _rotation, _width, _height, Side.Left));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX,
			final double minY, final double maxX, final double maxY) {

		_rotation.setForRuntime(runtime, 0);
		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2,
				(minY + maxY) / 2);
		_width.setForRuntime(runtime, Math.abs(maxX - minX));
		_height.setForRuntime(runtime, Math.abs(maxY - minY));
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime,
			final GeneralPath.Double target) {
		final double width2 = _width.getValueForRuntime(runtime) / 2;
		final double height2 = _height.getValueForRuntime(runtime) / 2;
		final double x = _centerPoint.getXValueForRuntime(runtime);
		final double y = _centerPoint.getYValueForRuntime(runtime);

		target.moveTo(-width2, -height2);
		target.lineTo(width2, -height2);
		target.lineTo(width2, height2);
		target.lineTo(-width2, height2);
		target.closePath();
		target.transform(AffineTransform.getRotateInstance(_rotation
				.getValueForRuntime(runtime)));

		target.transform(AffineTransform.getTranslateInstance(x, y));
	}

	@Override
	public Rotator getRotator() {
		return _rotator;
	}

	@Override
	public Scaler getScaler() {
		return _scaler;
	}

	@Override
	public Translator getTranslator() {
		return _translator;
	}

	void refresh(final Runtime runtime) {

	}

	@Override
	public Outline getOutline() {
		return _outline;
	}

	static class PictureAnchor extends BaseAnchor {

		static enum Side {
			Top(0, -1), Right(1, 0), Bottom(0, 1), Left(-1, 0), TopLeft(-1, -1), TopRight(
					1, -1), BottomRight(1, 1), BottomLeft(-1, 1);

			final int x;
			final int y;
			final int projectionMultiplier;

			Side(final int x, final int y) {
				this.x = x;
				this.y = y;
				this.projectionMultiplier = x == 0 || y == 0 ? 1 : 0;
			}
		}

		private final PictureForm _form;
		private final StaticPoint _center;
		private final StaticAngle _rotation;
		private final StaticLength _width;
		private final StaticLength _height;
		private final Side _side;

		public PictureAnchor(final PictureForm form, final IdentityToken id,
				final Name name, final StaticPoint center,
				final StaticAngle rotation, final StaticLength width,
				final StaticLength height, final Side side) {
			super(id, name);
			_form = form;
			_center = center;
			_rotation = rotation;
			_width = width;
			_height = height;
			_side = side;
		}

		@Override
		public void translate(final Runtime runtime, final double deltaX,
				final double deltaY) {
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

			final double newX = oldX
					+ Vector.projectionX(deltaX, deltaY,
							_side.projectionMultiplier * oldDeltaX,
							_side.projectionMultiplier * oldDeltaY);
			final double newY = oldY
					+ Vector.projectionY(deltaX, deltaY,
							_side.projectionMultiplier * oldDeltaX,
							_side.projectionMultiplier * oldDeltaY);

			final double newCenterX = (oppX + newX) / 2;
			final double newCenterY = (oppY + newY) / 2;

			final double newHalfWidth = Vector.getRotatedX(newX - newCenterX,
					newY - newCenterY, -oldRotation);
			final double newHalfHeight = Vector.getRotatedY(newX - newCenterX,
					newY - newCenterY, -oldRotation);

			final double newWidth = 2 * (oldHalfWidth + _side.x
					* (newHalfWidth - _side.x * oldHalfWidth));

			final double newHeight = 2 * (oldHalfHeight + _side.y
					* (newHalfHeight - _side.y * oldHalfHeight));

			_width.setForRuntime(runtime, newWidth);
			_height.setForRuntime(runtime, newHeight);

			_center.setForRuntime(runtime, newCenterX, newCenterY);

			_form.refresh(runtime);
		}
	}

	static class PictureTranslator implements Translator {

		private final PictureForm _form;
		private final Translator _otherTranslator;

		public PictureTranslator(final PictureForm form,
				final Translator translator) {
			_form = form;
			_otherTranslator = translator;
		}

		@Override
		public void translate(final Runtime runtime, final double deltaX,
				final double deltaY) {
			_otherTranslator.translate(runtime, deltaX, deltaY);
			_form.refresh(runtime);
		}

	}

	static class PictureRotator implements Rotator {

		private final PictureForm _form;
		private final Rotator _otherRotator;

		public PictureRotator(final PictureForm form, final Rotator rotator) {
			_form = form;
			_otherRotator = rotator;
		}

		@Override
		public void rotate(final Runtime runtime, final double angle,
				final double fixX, final double fixY) {
			_otherRotator.rotate(runtime, angle, fixX, fixY);
			_form.refresh(runtime);
		}

	}

	static class PictureScaler implements Scaler {

		private final PictureForm _form;
		private final Scaler _otherScaler;

		public PictureScaler(final PictureForm form, final Scaler scaler) {
			_form = form;
			_otherScaler = scaler;
		}

		@Override
		public void scale(final Runtime runtime, final double factor,
				final double fixX, final double fixY, final double directionX,
				final double directionY) {
			_otherScaler.scale(runtime, factor, fixX, fixY, directionX,
					directionY);
			_form.refresh(runtime);
		}

	}

}
