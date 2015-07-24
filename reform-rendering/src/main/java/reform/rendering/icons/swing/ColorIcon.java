package reform.rendering.icons.swing;

import javax.swing.*;
import java.awt.*;

public class ColorIcon implements javax.swing.Icon
{

	private final Color _transparentGrey = new Color(0x88111111, true);
	private final Font _font = new JPanel().getFont().deriveFont(Font.BOLD, 20);
	private Color _color;
	private final int _size;

	public ColorIcon(final Color color, final int size)
	{
		_color = color;
		_size = size;
	}

	public ColorIcon(final Color color)
	{
		this(color, 24);
	}

	@Override
	public void paintIcon(final Component c, final Graphics g, final int x, final int y)
	{
		if (c.isEnabled())
		{
			final Graphics2D g2 = (Graphics2D) g.create();

			final int rows = 4;
			final int cellSize = _size / rows;
			final int cols = _size / cellSize;


				g2.setClip(0, 0, _size, _size);

				for (int j = 0; j <= cols; j++)
				{
					for (int i = 0; i <= rows; i++)
					{
						if (i % 2 == j % 2)
						{
							g2.setColor(Color.GRAY);
						}
						else
						{
							g2.setColor(Color.WHITE);
						}
						g2.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
					}
				}
			if (_color != null)
			{
				g2.setColor(_color);
				g2.fillRect(x, y, _size, _size);

			}
			else
			{
				g2.setColor(_transparentGrey);
				g2.fillRect(x, y, _size, _size);

				g2.setColor(Color.WHITE);
				g2.setFont(_font);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawString("?", _size / 2 - 5, _size / 2 + 8);
			}

			g2.setColor(Color.GRAY);
			g2.drawRect(0, 0, _size - 1, _size - 1);

			g2.dispose();
		}
	}

	@Override
	public int getIconWidth()
	{
		return _size;
	}

	@Override
	public int getIconHeight()
	{
		return _size;
	}

	public void setColor(final Color color)
	{
		_color = color;
	}

}
