package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ActionTrashIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionTrashIcon()
	{
		_shape.add(new Area(new Rectangle2D.Double(-325, -200, 650, 620)));
		_shape.add(new Area(new Rectangle2D.Double(-400, -370, 800, 120)));
		_shape.add(new Area(new Rectangle2D.Double(-100, -470, 200, 100)));
		_shape.subtract(new Area(new Rectangle2D.Double(-200, -100, 70, 420)));
		_shape.subtract(new Area(new Rectangle2D.Double(130, -100, 70, 420)));
		_shape.subtract(new Area(new Rectangle2D.Double(-35, -100, 70, 420)));
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
