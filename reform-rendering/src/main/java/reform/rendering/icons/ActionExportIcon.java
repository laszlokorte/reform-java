package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ActionExportIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionExportIcon()
	{

		final AffineTransform t = AffineTransform.getRotateInstance(-Math.PI / 4);

		_shape.add(new Area(new Ellipse2D.Double(-400, -810, 1000, 1000)));
		_shape.subtract(new Area(new Ellipse2D.Double(-400, -1050, 1000, 1000)));
		_shape.subtract(new Area(new Rectangle.Double(100, -400, 500, 700)));

		final Area tip = new Area(t.createTransformedShape(new Rectangle2D.Double(-250, -250, 500, 500)));
		tip.subtract(new Area(new Rectangle2D.Double(-400, -400, 400, 800)));

		_shape.add(tip.createTransformedArea(AffineTransform.getTranslateInstance(100, 60)));
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
