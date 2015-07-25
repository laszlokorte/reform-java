package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ToolTranslateIcon implements Icon
{

	private final Area _shape;

	public ToolTranslateIcon()
	{
		final AffineTransform t = AffineTransform.getTranslateInstance(0, 0);
		t.rotate(-Math.PI / 4);

		_shape = new Area(
				t.createTransformedShape(new Rectangle2D.Double(-350, -350, 700, 700)));
		_shape.subtract(new Area(new Rectangle2D.Double(-300, -300, 600, 600)));
		_shape.add(new Area(new Rectangle2D.Double(-450, -50, 900, 100)));
		_shape.add(new Area(new Rectangle2D.Double(-50, -450, 100, 900)));

	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / 1000.0,
		                                                           width / 1000.0);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}

}
