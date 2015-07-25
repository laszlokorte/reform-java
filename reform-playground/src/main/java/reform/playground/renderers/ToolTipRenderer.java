package reform.playground.renderers;

import reform.math.Vec2i;

import javax.swing.*;
import java.awt.*;

public class ToolTipRenderer
{
	private final Color _background = new Color(0xbb222222, true);
	private final Vec2i _offset = new Vec2i(15, -15);
	private final FontMetrics _fontMetrics;
	private final Font _font = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

	public ToolTipRenderer()
	{
		final JComponent c = new JPanel();
		_fontMetrics = c.getFontMetrics(_font);
	}

	public void render(final Graphics2D g2, double x, double y, final String text, final Color color)
	{
		x = (int) (x / 6) * 6;
		y = (int) (y / 6) * 6;
		final int width = _fontMetrics.stringWidth(text) + 20;

		g2.setColor(_background);
		g2.setFont(_font);
		g2.fillRoundRect((int) x + _offset.x, (int) y + _offset.y, width, 30, 6, 6);
		g2.setColor(color);
		g2.drawString(text, (int) x + _offset.x + 10, (int) y + _offset.y + 20);
	}

	public void renderCentered(final Graphics2D g2, double x, double y, final String text, final Color color)
	{
		x = (int) (x / 6) * 6;
		y = (int) (y / 6) * 6;
		final int width = _fontMetrics.stringWidth(text) + 20;
		g2.setColor(_background);
		g2.setFont(_font);
		g2.fillRoundRect((int) x + _offset.x - width / 2, (int) y + _offset.y, width, 30, 6, 6);
		g2.setColor(color);
		g2.drawString(text, (int) x + _offset.x + 10 - width / 2, (int) y + _offset.y + 20);
	}
}
