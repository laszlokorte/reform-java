package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ToolRepairIcon implements Icon
{

	private final Shape _shape;

	public ToolRepairIcon()
	{
		final AffineTransform t = AffineTransform.getTranslateInstance(0, 0);
		t.rotate(-Math.PI / 4);

		final Area shape = new Area();
		shape.add(new Area(new Rectangle2D.Double(-400, -80, 800, 160)));
		shape.add(new Area(new Ellipse2D.Double(-600, -225, 450, 450)));
		shape.add(new Area(new Ellipse2D.Double(150, -225, 450, 450)));

		shape.subtract(new Area(new Rectangle2D.Double(-600, -100, 250, 200)));
		shape.subtract(new Area(new Rectangle2D.Double(350, -100, 250, 200)));


		_shape = t.createTransformedShape(shape);

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
