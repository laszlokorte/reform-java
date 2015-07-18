package reform.core.forms;

import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

import reform.core.forms.anchors.OrthogonalLengthAnchor;
import reform.core.forms.anchors.StaticPointAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.ConstantAngle;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.OffsetCenterPoint;
import reform.core.forms.relations.StaticLength;
import reform.core.forms.relations.StaticPoint;
import reform.core.forms.transformation.AbsoluteScaler;
import reform.core.forms.transformation.BasicLengthScaler;
import reform.core.forms.transformation.BasicPointRotator;
import reform.core.forms.transformation.BasicPointScaler;
import reform.core.forms.transformation.BasicTranslator;
import reform.core.forms.transformation.CompositeScaler;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

public class ArcForm extends BaseForm<ArcForm> {
	static private final int SIZE = 5;

	private final transient StaticPoint _startPoint = new StaticPoint(getId(),
			0);
	private final transient StaticPoint _endPoint = new StaticPoint(getId(), 2);
	private final transient StaticLength _offset = new StaticLength(getId(), 4);

	private final transient Translator _translator = new BasicTranslator(
			_startPoint, _endPoint);

	private final transient Rotator _rotator = new BasicPointRotator(
			_startPoint, _endPoint);

	private final transient Scaler _scaler = new CompositeScaler(
			new BasicPointScaler(_startPoint), new BasicPointScaler(_endPoint),
			new AbsoluteScaler(
					new BasicLengthScaler(_offset, new ConstantAngle(0), 0)));

	private final Outline _outline = new NullOutline();

	public static enum Point implements ExposedPointToken<ArcForm> {
		Center(0), Start(1), End(2);

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
		Center(0), Start(1), End(2);

		private final int _v;

		Anchor(final int i) {
			_v = i;
		}

		@Override
		public int getValue() {
			return _v;
		}
	}

	public static ArcForm construct(final Identifier<ArcForm> id,
			final Name name) {
		return new ArcForm(id, name);
	}

	private ArcForm(final Identifier<ArcForm> id, final Name name) {
		super(id, SIZE, name);
		addSnapPoint(
				new ExposedPoint(_startPoint, new Name("Start"), Point.Start));

		addSnapPoint(new ExposedPoint(_endPoint, new Name("End"), Point.End));

		addSnapPoint(new ExposedPoint(
				new OffsetCenterPoint(_startPoint, _endPoint, _offset),
				new Name("Center"), Point.Center));

		addAnchor(new StaticPointAnchor(Anchor.Start, new Name("Start"),
				_startPoint));
		addAnchor(
				new StaticPointAnchor(Anchor.End, new Name("End"), _endPoint));

		addAnchor(new OrthogonalLengthAnchor(Anchor.Center,
				new Name("ControlPoint"), _offset, _startPoint, _endPoint));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX,
			final double minY, final double maxX, final double maxY) {
		_startPoint.setForRuntime(runtime, minX, minY);
		_endPoint.setForRuntime(runtime, maxX, maxY);
		_offset.setForRuntime(runtime, 50);
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime,
			final GeneralPath.Double target) {
		final double offset = _offset.getValueForRuntime(runtime);
		final double startX = _startPoint.getXValueForRuntime(runtime);
		final double startY = _startPoint.getYValueForRuntime(runtime);
		final double endX = _endPoint.getXValueForRuntime(runtime);
		final double endY = _endPoint.getYValueForRuntime(runtime);

		final double distance2 = Vector.distance2(startX, startY, endX, endY);
		final double absRad = Math.sqrt(offset * offset + distance2 / 4);

		final double midX = (endX + startX) / 2;
		final double midY = (endY + startY) / 2;
		final double deltaX = (endX - startX) / 2;
		final double deltaY = (endY - startY) / 2;
		final double deltaLength = Math.sqrt(distance2) / 2;

		final double orthoDxNorm = Vector.orthogonalX(deltaX, deltaY)
				/ deltaLength;
		final double orthoDyNorm = Vector.orthogonalY(deltaX, deltaY)
				/ deltaLength;

		final double centerX = midX - orthoDxNorm * offset;
		final double centerY = midY - orthoDyNorm * offset;

		double angleB = Vector.angle(startX, startY, centerX, centerY);
		double angleA = Vector.angle(endX, endY, centerX, centerY);

		if (angleB < angleA) {
			final double tmp = angleA;
			angleA = angleB;
			angleB = tmp - 2 * Math.PI;
		}

		_arc.setArc(centerX - absRad, centerY - absRad, 2 * absRad, 2 * absRad,
				Math.toDegrees(angleA), Math.toDegrees(angleB - angleA),
				Arc2D.CHORD);

		target.append(_arc, false);
	}

	private final Arc2D.Double _arc = new Arc2D.Double();

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

}
