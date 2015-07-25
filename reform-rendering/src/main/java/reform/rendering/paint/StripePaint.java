package reform.rendering.paint;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface StripePaint
{

	static Paint getPaint()
	{
		final Color lightBackground = new Color(0xdddddd);

		final BufferedImage stripes = new BufferedImage(10, 10,
		                                                BufferedImage.TYPE_3BYTE_BGR);
		final TexturePaint paint = new TexturePaint(stripes, new Rectangle(0, 0, 10,
		                                                                   10));

		{
			final Graphics g = stripes.getGraphics();
			final int width = stripes.getWidth();
			final int height = stripes.getHeight();
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
			g.setColor(lightBackground);
			g.drawLine(0, 0, width, height);
		}

		return paint;
	}

}
