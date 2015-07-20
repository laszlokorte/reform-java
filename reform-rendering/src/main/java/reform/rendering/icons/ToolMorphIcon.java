package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class ToolMorphIcon implements Icon
{

	private final Shape _shape;

	public ToolMorphIcon()
	{
		final GeneralPath cursor = new GeneralPath();
		final Area shape = new Area();
		cursor.moveTo(-230.0, -460);
		cursor.lineTo(330.0, 100.0);
		cursor.lineTo(60.0, 85.0);
		cursor.lineTo(210.0, 415.0);
		cursor.lineTo(115.0, 460.0);
		cursor.lineTo(-45.0, 120.0);
		cursor.lineTo(-230.0, 300.0);
		cursor.closePath();

		shape.add(new Area(cursor));

		final Area dot = new Area(new Ellipse2D.Double(-430, -650, 400, 400));
		dot.subtract(new Area(new Ellipse2D.Double(-350, -570, 240, 240)));
		dot.subtract(new Area(cursor));
		shape.add(dot);
		shape.subtract(new Area(new Ellipse2D.Double(-270, -490, 80, 80)));

		_shape = AffineTransform.getTranslateInstance(100, 100).createTransformedShape(
				AffineTransform.getScaleInstance(0.85, 0.85).createTransformedShape(shape));

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
