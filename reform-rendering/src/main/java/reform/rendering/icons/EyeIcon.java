package reform.rendering.icons;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class EyeIcon implements Icon {

	private final Area _shape = new Area();

	public EyeIcon() {
		_shape.add(new Area(new Ellipse2D.Double(-500, -710, 1000, 1000)));
		_shape.intersect(
				new Area(new Ellipse2D.Double(-500, -290, 1000, 1000)));
		_shape.subtract(new Area(new Ellipse2D.Double(-190, -190, 380, 380)));
		_shape.add(new Area(new Ellipse2D.Double(-100, -100, 200, 200)));
	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y,
			final int width) {
		final AffineTransform t = AffineTransform
				.getScaleInstance(width / 1000.0, width / 1000.0);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}
}
