package reform.core.forms;

import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.anchors.OrthogonalLengthAnchor;
import reform.core.forms.anchors.StaticPointAnchor;
import reform.core.forms.outline.NullOutline;
import reform.core.forms.outline.Outline;
import reform.core.forms.relations.*;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.transformation.*;
import reform.core.graphics.ColoredShape;
import reform.core.runtime.Runtime;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public final class TextForm extends BaseForm<TextForm>
{

	public static final int ANCHOR_START = 0;
	public static final int ANCHOR_END = 1;
	public static final int ANCHOR_CENTER = 2;
	private final static Value DEFAULT_TEXT = new Value("Lorem ipsum");
	private final static Value DEFAULT_SCALE = new Value(1);
	static private final int SIZE = 5;
	private final static AffineTransform _trans = new AffineTransform();
	private static final Font _font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	private static final FontMetrics _metrics = new JLabel().getFontMetrics(_font);
	private final transient StaticPoint _startPoint = new StaticPoint(getId(), 0);
	private final transient StaticPoint _endPoint = new StaticPoint(getId(), 2);
	private final transient StaticLength _height = new StaticLength(getId(), 4);
	private final transient Translator _translator = new BasicTranslator(_startPoint,
	                                                                     _endPoint);
	private final transient Rotator _rotator = new BasicPointRotator(_startPoint,
	                                                                 _endPoint);
	private final transient Scaler _scaler = new CompositeScaler(
			new BasicPointScaler(_startPoint, _endPoint), new AbsoluteScaler(
			new BasicLengthScaler(_height, new ConstantRotationAngle(0), 0)));
	private final Outline _outline = new NullOutline();
	private final Attribute _textColorAttribute = new Attribute("Text Color",
	                                                            Attribute.Type.Color,
	                                                            DEFAULT_STROKE_COLOR);
	private final Attribute _fontSizeAttribute = new Attribute("Relative Font Size",
	                                                           Attribute.Type.Number,
	                                                           DEFAULT_SCALE);
	private final Attribute _textAttribute = new Attribute("Text", Attribute.Type.String,
	                                                       DEFAULT_TEXT);
	private final AttributeSet _attributes = new AttributeSet(_textColorAttribute,
	                                                          _fontSizeAttribute,
	                                                          _textAttribute);

	private TextForm(final Identifier<TextForm> id, final Name name)
	{
		super(id, SIZE, name);
		addSnapPoint(new ExposedPoint(_startPoint, new Name("Start"), Point.Start));
		addSnapPoint(new ExposedPoint(_endPoint, new Name("End"), Point.End));
		addSnapPoint(
				new ExposedPoint(new OffsetCenterPoint(_startPoint, _endPoint, _height),
				                 new Name("Top"), Point.Top));
		addSnapPoint(new ExposedPoint(new CenterPoint(_startPoint, _endPoint),
		                              new Name("Bottom"), Point.Bottom));


		addAnchor(new StaticPointAnchor(Anchor.Start, new Name("Start"), _startPoint));
		addAnchor(new StaticPointAnchor(Anchor.End, new Name("End"), _endPoint));

		addAnchor(
				new OrthogonalLengthAnchor(Anchor.Top, new Name("ControlPoint"), _height,
				                           _startPoint, _endPoint));
	}

	@Override
	public void initialize(final Runtime runtime, final double minX, final double minY,
	                       final double maxX, final double maxY)
	{
		_startPoint.setForRuntime(runtime, minX, minY);
		_endPoint.setForRuntime(runtime, maxX, maxY);
		_height.setForRuntime(runtime, 16);
	}

	@Override
	public void appendToPathForRuntime(final Runtime runtime, final GeneralPath.Double
			target)
	{
		final DataSet dataSet = runtime.getDataSet();

		final GlyphVector v = _font.createGlyphVector(_metrics.getFontRenderContext(),
		                                              _textAttribute.getValue()
				                                              .getValueFor(dataSet)
				                                              .getString());

		final double endX = _endPoint.getXValueForRuntime(runtime);
		final double endY = _endPoint.getYValueForRuntime(runtime);
		final double startX = _startPoint.getXValueForRuntime(runtime);
		final double startY = _startPoint.getYValueForRuntime(runtime);
		final double scale = _fontSizeAttribute.getValue().getValueFor(
				dataSet).getDouble();
		final double height = _height.getValueForRuntime(runtime) / 16;


		final Shape s = v.getOutline();
		_trans.setToIdentity();
		_trans.translate((startX + endX) / 2, (startY + endY) / 2);
		_trans.rotate(-Vector.angle(endX, endY, startX, startY));
		_trans.scale(height * scale, height * scale);
		_trans.translate(-s.getBounds().getWidth() / 2, 0);

		target.append(_trans.createTransformedShape(s), false);
	}

	@Override
	public void writeColoredShapeForRuntime(final Runtime runtime, final ColoredShape
			coloredShape)
	{
		final DataSet dataSet = runtime.getDataSet();

		coloredShape.setBackgroundColor(
				_textColorAttribute.getValue().getValueFor(dataSet).getColor());
		coloredShape.setStrokeColor(0);
		coloredShape.setStrokeWidth(
				_fontSizeAttribute.getValue().getValueFor(dataSet).getInteger());

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

	public static TextForm construct(final Identifier<TextForm> id, final Name name)
	{
		return new TextForm(id, name);
	}

	public enum Point implements ExposedPointToken<TextForm>
	{
		Top(1), Start(2), End(3), Bottom(4);

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
		Top(1), Start(2), End(3);

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
}
