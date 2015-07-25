package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ActionDeleteRowIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionDeleteRowIcon()
	{
		_shape.add(new Area(new Rectangle2D.Double(-500, -300, 400, 100)));
		_shape.add(new Area(new Rectangle2D.Double(0, -300, 600, 100)));


		_shape.add(new Area(new Rectangle2D.Double(0, -100, 600, 100)));


		final Area cross = new Area();
		cross.add(new Area(new Rectangle2D.Double(-225, -50, 450, 100)));
		cross.add(new Area(new Rectangle2D.Double(-50, -225, 100, 450)));


		final AffineTransform t = AffineTransform.getTranslateInstance(-300, 150);
		t.rotate(Math.PI / 4);
		_shape.add(cross.createTransformedArea(t));


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
