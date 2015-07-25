package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ToolScaleIcon implements Icon
{

	private final Area _shape;

	public ToolScaleIcon()
	{
		final AffineTransform t = AffineTransform.getTranslateInstance(0, 0);
		t.rotate(-Math.PI / 4);

		_shape = new Area(new Rectangle2D.Double(-400, -400, 800, 800));
		_shape.subtract(new Area(
				t.createTransformedShape(new Rectangle2D.Double(-330, -630, 660, 1260)
				)));
		_shape.add(new Area(
				t.createTransformedShape(new Rectangle2D.Double(-500, -50, 1000, 100))));

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
