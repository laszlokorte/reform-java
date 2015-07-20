package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class RulerIcon implements Icon
{

	private final Shape _shape;

	public RulerIcon()
	{
		final Area shape = new Area();
		shape.add(new Area(new Rectangle2D.Double(-705, -250, 1410, 500)));
		shape.subtract(new Area(new Rectangle2D.Double(-605, -150, 1210, 300)));

		for (int i = 0; i < 5; i++)
		{
			shape.add(new Area(new Rectangle2D.Double(-500 + 225 * i, -250, 100, i % 2 == 0 ? 300 : 200)));
		}

		_shape = AffineTransform.getRotateInstance(-Math.PI / 4).createTransformedShape(shape);
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
