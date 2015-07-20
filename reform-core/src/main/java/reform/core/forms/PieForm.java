package reform.core.forms;

import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.anchors.BaseAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.*;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.transformation.*;
import reform.core.graphics.Color;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public final class PieForm extends BaseForm<PieForm>
{
	static private final int SIZE = 5;

	private final transient StaticPoint _centerPoint = new StaticPoint(getId(), 0);
	private final transient StaticLength _radius = new StaticLength(getId(), 2);
	private final transient StaticAngle _angleUpperBound = new StaticAngle(getId(), 3);
	private final transient StaticAngle _angleLowerBound = new StaticAngle(getId(), 4);

	private final transient Translator _translator = new BasicTranslator(_centerPoint);

	private final transient Rotator _rotator = new CompositeRotator(new BasicPointRotator(_centerPoint), new
			BasicAngleRotator(_angleUpperBound), new BasicAngleRotator(_angleLowerBound));

	private final transient Scaler _scaler = new CompositeScaler(new BasicPointScaler(_centerPoint), new
			AbsoluteScaler(new BasicLengthScaler(_radius, _angleUpperBound, 0)));

	private final Outline _outline = new NullOutline();

	private final Attribute<Color> _fillColorAttribute = new Attribute<>("Fill", Color.class, Form.DEFAULT_FILL);
	private final Attribute<Color> _strokeColorAttribute = new Attribute<>("Stroke", Color.class, Form.DEFAULT_STROKE);

	private final AttributeSet _attributes = new AttributeSet(_fillColorAttribute, _strokeColorAttribute);

	public enum Point implements ExposedPointToken<PieForm>
	{
		Center(0), Start(1), End(2);

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
		Start(1), End(2);

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

	public static PieForm construct(final Identifier<PieForm> id, final Name name)
	{
		return new PieForm(id, name);
	}

	private PieForm(final Identifier<PieForm> id, final Name name)
	{
		super(id, SIZE, name);
		addSnapPoint(new ExposedPoint(_centerPoint, new Name("Center"), Point.Center));
		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(new ComposedCartesianPoint
				(_radius, new ConstantLength(0)), _angleLowerBound)), new Name("Start"), Point.Start));

		addSnapPoint(new ExposedPoint(new SummedPoint(_centerPoint, new RotatedPoint(new ComposedCartesianPoint
				(_radius, new ConstantLength(0)), _angleUpperBound)), new Name("End"), Point.End));

		addAnchor(new PieCornerAnchor(Anchor.Start, new Name("Start"), _radius, _angleLowerBound));
		addAnchor(new PieCornerAnchor(Anchor.End, new Name("End"), _radius, _angleUpperBound));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX, final double minY, final double maxX, final
	double maxY)
	{
		final double dx = maxX - minX;
		final double dy = maxY - minY;

		_centerPoint.setForRuntime(runtime, (minX + maxX) / 2, (minY + maxY) / 2);
		_radius.setForRuntime(runtime, Math.sqrt(dx * dx + dy * dy) / 2);
		_angleUpperBound.setForRuntime(runtime, Vector.angleOf(dx, dy));

		_angleLowerBound.setForRuntime(runtime, Vector.angleOf(dx, dy) - Math.PI);
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime, final GeneralPath.Double target)
	{
		final double radius = _radius.getValueForRuntime(runtime);
		final double cx = _centerPoint.getXValueForRuntime(runtime);
		final double cy = _centerPoint.getYValueForRuntime(runtime);
		final double angleUpper = _angleUpperBound.getValueForRuntime(runtime);
		double angleLower = _angleLowerBound.getValueForRuntime(runtime);

		if (angleUpper < angleLower)
		{
			angleLower -= Math.PI * 2;
		}

		final double start = Math.toDegrees(angleLower);
		double extend = Math.toDegrees(angleUpper - angleLower);

		if (extend > 360)
		{
			extend -= 360;
		}
		else if (extend < -360)
		{
			extend += 360;
		}

		_arc.setArc(cx - radius, cy - radius, 2 * radius, 2 * radius, -start, -extend, Arc2D.PIE);

		target.append(_arc, false);

	}

	private final Arc2D.Double _arc = new Arc2D.Double();

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

	static class PieCornerAnchor extends BaseAnchor
	{

		private final StaticLength _radius;
		private final StaticAngle _angle;

		public PieCornerAnchor(final IdentityToken token, final Name name, final StaticLength radius, final
		StaticAngle angle)
		{
			super(token, name);
			_radius = radius;
			_angle = angle;
		}

		@Override
		public void translate(final Runtime runtime, final double x, final double y)
		{
			final double oldAngle = _angle.getValueForRuntime(runtime);
			final double oldRadius = _radius.getValueForRuntime(runtime);

			final double oldDeltaX = Vector.getRotatedX(oldRadius, 0, oldAngle);
			final double oldDeltaY = Vector.getRotatedY(oldRadius, 0, oldAngle);

			final double newDeltaX = oldDeltaX + x;
			final double newDeltaY = oldDeltaY + y;

			final double newRadius = Vector.length(newDeltaX, newDeltaY);

			final double newAngle = Vector.angleOf(newDeltaX, newDeltaY);

			_radius.setForRuntime(runtime, newRadius);
			_angle.setForRuntime(runtime, newAngle);
		}

	}

	@Override
	public AttributeSet getAttributes()
	{
		return _attributes;
	}

}
