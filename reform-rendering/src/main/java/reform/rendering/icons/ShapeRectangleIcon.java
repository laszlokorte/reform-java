package reform.rendering.icons;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ShapeRectangleIcon implements Icon {

	private final Area _shape = new Area();

	public ShapeRectangleIcon() {
		_shape.add(new Area(new Rectangle2D.Double(-500, -400, 1000, 800)));
		_shape.subtract(new Area(new Rectangle2D.Double(-400, -300, 800, 600)));
	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y,
			final int width) {
		final AffineTransform t = AffineTransform.getScaleInstance(
				width / 1000.0, width / 1000.0);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}

}
