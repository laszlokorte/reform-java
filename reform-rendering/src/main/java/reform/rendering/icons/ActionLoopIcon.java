package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ActionLoopIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionLoopIcon()
	{
		final AffineTransform t = AffineTransform.getRotateInstance(-Math.PI / 4);
		final AffineTransform t2 = AffineTransform.getTranslateInstance(0, -300);
		final AffineTransform t3 = AffineTransform.getRotateInstance(-Math.PI);
		final AffineTransform t4 = AffineTransform.getTranslateInstance(0, 300);
		_shape.add(new Area(new Ellipse2D.Double(-400, -400, 800, 800)));
		_shape.subtract(new Area(new Ellipse2D.Double(-250, -250, 500, 500)));
		_shape.subtract(new Area(t.createTransformedShape(new Rectangle2D.Double(-500, -150, 1000, 300))));

		final Area tip = new Area(t.createTransformedShape(new Rectangle2D.Double(-175, -175, 350, 350)));
		tip.subtract(new Area(new Rectangle2D.Double(-300, -300, 300, 600)));

		_shape.add(tip.createTransformedArea(t2));
		_shape.add(tip.createTransformedArea(t3).createTransformedArea(t4));
		_shape.add(new Area(new Rectangle2D.Double(350, -350, 350, 120)));
		_shape.add(new Area(new Rectangle2D.Double(465, -465, 120, 350)));

		_shape.transform(AffineTransform.getTranslateInstance(-130, 0));
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
