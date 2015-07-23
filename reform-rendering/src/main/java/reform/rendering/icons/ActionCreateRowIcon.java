package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ActionCreateRowIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionCreateRowIcon()
	{
		_shape.add(new Area(new Rectangle2D.Double(-500, -300, 400, 100)));
		_shape.add(new Area(new Rectangle2D.Double(0, -300, 600, 100)));

		_shape.add(new Area(new Rectangle2D.Double(-500, -100, 400, 100)));
		_shape.add(new Area(new Rectangle2D.Double(0, -100, 600, 100)));

		_shape.add(new Area(new Rectangle2D.Double(0, 100, 600, 100)));


		Area cross = new Area();
		cross.add(new Area(new Rectangle2D.Double(-200, -50, 400, 100)));
		cross.add(new Area(new Rectangle2D.Double(-50, -200, 100, 400)));


		AffineTransform t = AffineTransform.getTranslateInstance(-300,300);
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
