package reform.rendering.icons.swing;

import reform.rendering.icons.Icon;

import java.awt.*;

public class ColorIcon implements javax.swing.Icon {

	private Color _color;
	private final int _size;

	public ColorIcon(final Color color, final int size) {
        _color = color;
        _size = size;
	}

	@Override
	public void paintIcon(final Component c, final Graphics g, final int x,
			final int y) {
		if(c.isEnabled()) {
            final Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(_color
            );
            g2.fillRect(x,y, _size, _size);
            g2.dispose();
        }
	}

	@Override
	public int getIconWidth() {
		return _size;
	}

	@Override
	public int getIconHeight() {
		return _size;
	}

    public  void setColor(Color color) {
        _color = color;
    }

}
