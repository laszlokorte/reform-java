package reform.core.forms;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.CircleOutline;
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

public class CircleForm extends BaseForm<CircleForm> {
	static private final int SIZE = 4;

	private final transient StaticPoint _centerPoint = new StaticPoint(getId(),
			0);
	private final transient StaticLength _radius = new StaticLength(getId(), 2);
	private final transient StaticAngle _rotation = new StaticAngle(getId(), 3);

	private final transient Translator _translator = new BasicTranslator(
			_centerPoint);

	private final transient Rotator _rotator = new CompositeRotator(
			new BasicPointRotator(_centerPoint), new BasicAngleRotator(
					_rotation));

	private final transient Scaler _scaler = new CompositeScaler(
			new BasicPointScaler(_centerPoint), new BasicLengthScaler(_radius,
					_rotation, 0));

	private final Outline _outline = new CircleOutline(_centerPoint, _radius,
			_rotation);

	public static enum Point implements ExposedPointToken<CircleForm> {
		Center(0), Top(1), Right(2), Bottom(3), Left(4);

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
		Top(1), Right(2), Bottom(3), Left(4);

		private final int _v;

		Anchor(final int i) {
			_v = i;
		}

		@Override
		public int getValue() {
			return _v;
		}
	}

	public static CircleForm construct(final Identifier<CircleForm> id,
			final Name name) {
		return new CircleForm(id, name);
	}

	private CircleForm(final Identifier<CircleForm> id, final Name name) {
		super(id, SIZE, name);
		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"),
				Point.Center));
		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(_radius,
						new ConstantLength(0)), _rotation)), new Name("Right"),
						Point.Right));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ConstantLength(
						0), _radius), _rotation)), new Name("Bottom"),
						Point.Bottom));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ScaledLength(
						_radius, -1), new ConstantLength(0)), _rotation)),
						new Name("Left"), Point.Left));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint,
				new RotatedPoint(new ComposedCartesianPoint(new ConstantLength(
						0), new ScaledLength(_radius, -1)), _rotation)),
						new Name("Top"), Point.Top));

		addAnchor(new CircleQuaterAnchor(Anchor.Top, new Name("Top"),
				CircleQuaterAnchor.Quater.North, _radius, _rotation));

		addAnchor(new CircleQuaterAnchor(Anchor.Right, new Name("Right"),
				CircleQuaterAnchor.Quater.East, _radius, _rotation));

		addAnchor(new CircleQuaterAnchor(Anchor.Bottom, new Name("Bottom"),
				CircleQuaterAnchor.Quater.South, _radius, _rotation));

		addAnchor(new CircleQuaterAnchor(Anchor.Left, new Name("Left"),
				CircleQuaterAnchor.Quater.West, _radius, _rotation));

	}

	@Override
	public void initialize(final Runtime runtime, final double minX,
			final double minY, final double maxX, final double maxY) {
		final double dx = maxX - minX;
		final double dy = maxY - minY;

		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2,
				(minY + maxY) / 2);
		_radius.setForRuntime(runtime, Math.sqrt(dx * dx + dy * dy) / 2);
		_rotation.setForRuntime(runtime, Math.atan2(-dy, -dx));
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime,
			final GeneralPath.Double target) {
		final double radius = _radius.getValueForRuntime(runtime);
		final double cx = _centerPoint.getXValueForRuntime(runtime);
		final double cy = _centerPoint.getYValueForRuntime(runtime);

		_circle.setFrame(cx - radius, cy - radius, 2 * radius, 2 * radius);
		target.append(_circle, false);
	}

	private final Ellipse2D.Double _circle = new Ellipse2D.Double();

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

	@Override
	public Outline getOutline() {
		return _outline;
	}

	static class CircleQuaterAnchor extends BaseAnchor {

		static enum Quater {
			North(-Math.PI / 2), East(0), South(-3 * Math.PI / 2), West(Math.PI);

			final double angle;

			private Quater(final double angle) {
				this.angle = angle;
			}
		}

		private final Quater _quater;
		private final StaticLength _radius;
		private final StaticAngle _angle;

		public CircleQuaterAnchor(final IdentityToken token, final Name name,
				final Quater quater, final StaticLength radius,
				final StaticAngle angle) {
			super(token, name);
			_quater = quater;
			_radius = radius;
			_angle = angle;
		}

		@Override
		public void translate(final Runtime runtime, final double x,
				final double y) {
			final double oldAngle = _angle.getValueForRuntime(runtime);
			final double oldRadius = _radius.getValueForRuntime(runtime);

			final double oldDeltaX = Vector.getRotatedX(oldRadius, 0, oldAngle
					+ _quater.angle);
			final double oldDeltaY = Vector.getRotatedY(oldRadius, 0, oldAngle
					+ _quater.angle);

			final double newDeltaX = oldDeltaX + x;
			final double newDeltaY = oldDeltaY + y;

			final double newRadius = Vector.length(newDeltaX, newDeltaY);

			final double newAngle = Vector.angleOf(newDeltaX, newDeltaY)
					- _quater.angle;

			_radius.setForRuntime(runtime, newRadius);
			_angle.setForRuntime(runtime, newAngle);
		}

	}

}