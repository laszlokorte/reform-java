package reform.core.forms;

import java.awt.geom.GeneralPath;

import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.anchors.StaticPointAnchor;
import reform.core.forms.outline.LineOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.CenterPoint;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.StaticPoint;
import reform.core.forms.transformation.BasicPointRotator;
import reform.core.forms.transformation.BasicPointScaler;
import reform.core.forms.transformation.BasicTranslator;
import reform.core.forms.transformation.Rotator;
import reform.core.forms.transformation.Scaler;
import reform.core.forms.transformation.Translator;
import reform.core.graphics.Color;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.naming.Name;

public final class LineForm extends BaseForm<LineForm> {

	static private final int SIZE = 4;

	private final transient StaticPoint _startPoint = new StaticPoint(getId(),
			0);
	private final transient StaticPoint _endPoint = new StaticPoint(getId(), 2);

	private final transient Translator _translator = new BasicTranslator(
			_startPoint, _endPoint);

	private final transient Rotator _rotator = new BasicPointRotator(
			_startPoint, _endPoint);

	private final transient Scaler _scaler = new BasicPointScaler(_startPoint,
			_endPoint);

	private final Outline _outline = new LineOutline(_startPoint, _endPoint);

    private final Attribute<Color> _strokeColorAttribute = new Attribute<>
            ("Color", Color.class, new Color());

    private final AttributeSet _attributes = new AttributeSet
            (_strokeColorAttribute);

	public enum Point implements ExposedPointToken<LineForm> {
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

	public enum Anchor implements IdentityToken {
		Start(1), End(2);

		private final int _v;

		Anchor(final int i) {
			_v = i;
		}

		@Override
		public int getValue() {
			return _v;
		}
	}

	public static final int ANCHOR_START = 0;
	public static final int ANCHOR_END = 1;
	public static final int ANCHOR_CENTER = 2;

	public static LineForm construct(final Identifier<LineForm> id,
			final Name name) {
		return new LineForm(id, name);
	}

	private LineForm(final Identifier<LineForm> id, final Name name) {
		super(id, SIZE, name);
		addSnapPoint(new ExposedPoint(_startPoint, new Name("Start"),
				Point.Start));
		addSnapPoint(new ExposedPoint(_endPoint, new Name("End"), Point.End));
		addSnapPoint(new ExposedPoint(new CenterPoint(_startPoint, _endPoint),
				new Name("Center"), Point.Center));

		addAnchor(new StaticPointAnchor(Anchor.Start, new Name("Start"),
				_startPoint));
		addAnchor(new StaticPointAnchor(Anchor.End, new Name("End"), _endPoint));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX,
			final double minY, final double maxX, final double maxY) {
		_startPoint.setForRuntime(runtime, minX, minY);
		_endPoint.setForRuntime(runtime, maxX, maxY);
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime,
			final GeneralPath.Double target) {

		target.moveTo(_startPoint.getXValueForRuntime(runtime),
                _startPoint.getYValueForRuntime(runtime));
		target.lineTo(_endPoint.getXValueForRuntime(runtime),
                _endPoint.getYValueForRuntime(runtime));
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

	@Override
	public Outline getOutline() {
		return _outline;
	}

    @Override
    public AttributeSet getAttributes() {
        return _attributes;
    }
}
