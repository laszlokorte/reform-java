package reform.rendering.icons;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

public class ShapeTextIcon implements Icon
{
	private final Shape _shape;
	private final double _size;

	public ShapeTextIcon()
	{
		final Font f = new Font(Font.SERIF, Font.PLAIN, 64);
		final Canvas c = new Canvas();
		final FontMetrics metrics = c.getFontMetrics(f);
		final GlyphVector v = f.createGlyphVector(metrics.getFontRenderContext(), "T");
		final Shape s = v.getOutline();
		final Rectangle bounds = s.getBounds();
		_size = Math.max(bounds.width, bounds.height) + 10;
		_shape = AffineTransform.getTranslateInstance(-bounds.x - bounds.width / 2, -bounds.y - bounds.height / 2)
				.createTransformedShape(s);
	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / _size, width / _size);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}

}
