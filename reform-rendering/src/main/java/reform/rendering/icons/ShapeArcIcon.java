package reform.rendering.icons;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

public class ShapeArcIcon implements Icon {

	private final Area _shape = new Area();

	public ShapeArcIcon() {
		final Arc2D.Double pie = new Arc2D.Double(-380, -380, 760, 760, 10,
				230, Arc2D.CHORD);
		final Stroke s = new BasicStroke(100);
		_shape.add(new Area(s.createStrokedShape(pie)));
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
