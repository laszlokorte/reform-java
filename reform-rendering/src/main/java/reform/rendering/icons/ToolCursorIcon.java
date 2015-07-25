package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class ToolCursorIcon implements Icon
{

	private final GeneralPath _shape = new GeneralPath();

	public ToolCursorIcon()
	{
		_shape.moveTo(-230.0, -460);
		_shape.lineTo(330.0, 100.0);
		_shape.lineTo(60.0, 85.0);
		_shape.lineTo(210.0, 415.0);
		_shape.lineTo(115.0, 460.0);
		_shape.lineTo(-45.0, 120.0);
		_shape.lineTo(-230.0, 300.0);
	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / 1000.0,
		                                                           width / 1000.0);
		g.translate(x, y);
		g.fill(_shape.createTransformedShape(t));
		g.translate(-x, -y);
	}

}
