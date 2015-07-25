package reform.rendering.icons.swing;

import reform.rendering.icons.Icon;

import javax.swing.*;
import java.awt.*;

public class SwingIcon implements javax.swing.Icon
{

	private final Icon _icon;
	private final int _size;
	private final boolean _smooth;
	private final Color _selectedColor;

	public SwingIcon(final Icon icon, final int size, final boolean smooth, final Color
			selectedColor)
	{
		_icon = icon;
		_size = size;
		_smooth = smooth;
		_selectedColor = selectedColor;
	}

	public SwingIcon(final Icon icon, final int size, final boolean smooth)
	{
		this(icon, size, smooth, new Color(0x23A9E5));
	}


	public SwingIcon(final Icon icon)
	{
		this(icon, true);
	}

	public SwingIcon(final Icon icon, final boolean smooth)
	{
		this(icon, 24, smooth);
	}


	@Override
	public void paintIcon(final Component c, final Graphics g, final int x, final int y)
	{
		final Graphics2D g2 = (Graphics2D) g.create();
		if (_smooth)
		{
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			                    RenderingHints.VALUE_ANTIALIAS_ON);
		}


		if (!c.isEnabled())
		{
			g2.setColor(Color.LIGHT_GRAY);
		}
		else if (c instanceof JToggleButton && ((JToggleButton) c).isSelected())
		{
			g2.setColor(_selectedColor);
		}
		else
		{
			g2.setColor(Color.BLACK);
		}
		_icon.draw(g2, x + _size / 2, 1 + y + _size / 2, _size - 4);
		g2.dispose();
	}

	@Override
	public int getIconWidth()
	{
		return _size;
	}

	@Override
	public int getIconHeight()
	{
		return _size + 2;
	}

}
