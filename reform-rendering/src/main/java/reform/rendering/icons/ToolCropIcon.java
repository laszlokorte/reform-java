package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ToolCropIcon implements Icon
{

	private final Area _shape = new Area();

	public ToolCropIcon()
	{
		final AffineTransform t = AffineTransform.getTranslateInstance(80, -60);
		t.rotate(-Math.PI / 4);

		_shape.add(new Area(new Rectangle2D.Double(-500 + 220, -500 + 50, 100, 800)));
		_shape.add(new Area(new Rectangle2D.Double(-500 + 750, -500 + 220, 100, 800)));

		_shape.add(new Area(new Rectangle2D.Double(-500 + 50, -500 + 220, 800, 100)));
		_shape.add(new Area(new Rectangle2D.Double(-500 + 220, -500 + 750, 800, 100)));

		_shape.add(new Area(
				t.createTransformedShape(new Rectangle2D.Double(-500, -30, 1000, 60))));

	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / 1000.0,
		                                                           width / 1000.0);
		g.translate(x, y);
		g.fill(_shape.createTransformedArea(t));
		g.translate(-x, -y);
	}

}
