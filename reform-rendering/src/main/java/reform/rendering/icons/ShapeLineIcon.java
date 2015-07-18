package reform.rendering.icons;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class ShapeLineIcon implements Icon {

	private final Shape _shape;

	public ShapeLineIcon() {
		final AffineTransform t = AffineTransform.getTranslateInstance(0, 0);
		t.rotate(-Math.PI / 4);

		_shape = t.createTransformedShape(new Rectangle2D.Double(-600, -50,
				1200, 100));

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
