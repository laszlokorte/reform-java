package reform.rendering.icons.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import reform.rendering.icons.Icon;

public class SwingIcon implements javax.swing.Icon {

	private final Icon _icon;
	private final int _size;
	private final boolean _smooth;

	public SwingIcon(final Icon icon, final int size, final boolean smooth) {
		_icon = icon;
		_size = size;
		_smooth = smooth;
	}

	public SwingIcon(final Icon icon) {
		this(icon, true);
	}

	public SwingIcon(final Icon icon, final boolean smooth) {
		this(icon, 24, smooth);
	}

	@Override
	public void paintIcon(final Component c, final Graphics g, final int x,
			final int y) {
		final Graphics2D g2 = (Graphics2D) g.create();
		if (_smooth) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		if (!c.isEnabled()) {
			g2.setColor(Color.LIGHT_GRAY);
		} else {
			g2.setColor(Color.BLACK);
		}
		_icon.draw(g2, x + _size / 2, y + _size / 2, _size - 4);
		g2.dispose();
	}

	@Override
	public int getIconWidth() {
		return _size;
	}

	@Override
	public int getIconHeight() {
		return _size;
	}

}
