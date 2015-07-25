package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.*;

public class ShapePictureIcon implements Icon
{

	private final Area _shape = new Area();

	public ShapePictureIcon()
	{
		final Stroke s = new BasicStroke(60);
		final GeneralPath p = new GeneralPath();

		p.moveTo(-450, 350);
		p.lineTo(-200, 100);
		p.lineTo(-50, 200);
		p.lineTo(200, -50);
		p.lineTo(450, 100);

		_shape.add(new Area(new Rectangle2D.Double(-500, -400, 1000, 800)));
		_shape.subtract(new Area(new Rectangle2D.Double(-400, -300, 800, 600)));
		_shape.add(new Area(new Ellipse2D.Double(-310, -240, 210, 210)));
		_shape.add(new Area(s.createStrokedShape(p)));

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
