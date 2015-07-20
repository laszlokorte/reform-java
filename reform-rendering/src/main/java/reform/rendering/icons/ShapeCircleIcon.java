package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class ShapeCircleIcon implements Icon
{

	private final Area _shape = new Area();

	public ShapeCircleIcon()
	{
		_shape.add(new Area(new Ellipse2D.Double(-450, -450, 900, 900)));
		_shape.subtract(new Area(new Ellipse2D.Double(-350, -350, 700, 700)));
	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / 1000.0, width / 1000.0);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}
}
