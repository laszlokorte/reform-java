package reform.rendering.assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A {@link Dot} is a runtime rendered monochromatic image of gradient filled
 * circle with a border.
 */
public class Dot implements Asset
{

	private final BufferedImage _bitmap;

	public Dot(final int radius, final float strokeWidth, final Color fillColor, final Color strokeColor)
	{
		_bitmap = new BufferedImage(radius * 2, radius * 2, BufferedImage.TYPE_4BYTE_ABGR);

		_prerender(strokeWidth, fillColor, strokeColor);
	}

	public Dot(final int radius, final float strokeWidth, final Color color)
	{
		this(radius, strokeWidth, color, color);
	}

	@Override
	public void drawAt(final Graphics g, final double x, final double y)
	{
		final int w = _bitmap.getWidth();
		final int h = _bitmap.getHeight();

		g.drawImage(_bitmap, (int) (Math.round(x) - w / 2), (int) (Math.round(y) - h / 2), w, h, null);
	}

	private void _prerender(final float strokeWidth, final Color fillColor, final Color strokeColor)
	{
		final int strokeWidthInt = (int) Math.ceil(strokeWidth);
		final Stroke stroke = new BasicStroke(strokeWidth);
		final Graphics2D g = (Graphics2D) _bitmap.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(new GradientPaint(0, 0, fillColor.brighter(), 0, _bitmap.getHeight(), fillColor));
		g.fillOval(strokeWidthInt, strokeWidthInt, _bitmap.getWidth() - 2 * strokeWidthInt, _bitmap.getHeight() - 2 *
				strokeWidthInt);
		g.setColor(strokeColor);
		g.setStroke(stroke);
		g.drawOval(strokeWidthInt, strokeWidthInt, _bitmap.getWidth() - 2 * strokeWidthInt, _bitmap.getHeight() - 2 *
				strokeWidthInt);
		g.setColor(strokeColor);
	}

}
