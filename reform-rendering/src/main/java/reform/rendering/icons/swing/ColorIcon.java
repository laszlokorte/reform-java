package reform.rendering.icons.swing;

import reform.rendering.icons.Icon;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        if (c.isEnabled()) {
            final Graphics2D g2 = (Graphics2D) g.create();

            int rows = 4;
            int cellSize = _size / rows;
            int cols = _size / cellSize;

            for (int j = 0; j <= cols; j++) {
                for (int i = 0; i <= rows; i++) {
                    if (i % 2 == j % 2) {
                        g2.setColor(Color.GRAY);
                    } else {
                        g2.setColor(Color.WHITE);
                    }
                    g2.fillRect(1 + j * cellSize, 1 + i * cellSize, cellSize, cellSize);
                }
            }
            g2.setColor(Color.GRAY);
            g2.drawRect(0,0,_size-1,_size-1);

            g2.setColor(_color
            );
            g2.fillRect(x, y, _size, _size);
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

    public void setColor(Color color) {
        _color = color;
    }

}