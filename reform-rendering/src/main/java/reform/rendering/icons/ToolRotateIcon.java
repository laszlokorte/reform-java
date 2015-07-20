package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.*;

public class ToolRotateIcon implements Icon
{

	private final Area _shape = new Area();

	public ToolRotateIcon()
	{
		_shape.add(new Area(new Ellipse2D.Double(-450, -450, 900, 900)));
		_shape.subtract(new Area(new Ellipse2D.Double(-350, -350, 700, 700)));
		_shape.subtract(new Area(new Rectangle2D.Double(-500, -450, 400, 1000)));
		_shape.subtract(new Area(new Rectangle2D.Double(-450, 50, 1000, 400)));

		final GeneralPath tip = new GeneralPath();
		tip.moveTo(0, -180);
		tip.lineTo(350, 0);
		tip.lineTo(0, 180);
		tip.closePath();

		final AffineTransform t1 = AffineTransform.getTranslateInstance(400, 0);
		final AffineTransform t2 = AffineTransform.getTranslateInstance(0, -400);
		t1.rotate(Math.PI / 2);
		t2.rotate(Math.PI);
		_shape.add(new Area(t1.createTransformedShape(tip)));
		_shape.add(new Area(t2.createTransformedShape(tip)));

		_shape.transform(AffineTransform.getRotateInstance(Math.PI / 20));
		_shape.transform(AffineTransform.getTranslateInstance(-80, 80));
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
