package reform.core.graphics;


import java.awt.*;
import java.awt.Color;
import java.awt.geom.GeneralPath;

public class ColoredShape
{
	private Stroke _stroke = new BasicStroke(1);
	private Color _backgroundColor;
	private Color _strokeColor;
	private GeneralPath.Double _path = new GeneralPath.Double();

	public Stroke getStroke() {
		return _stroke;
	}

	public Color getBackgroundColor() {
		return _backgroundColor;
	}

	public GeneralPath.Double getPath() {
		return _path;
	}

	public void setBackgroundColor(final reform.core.graphics.Color color) {
		_backgroundColor = new Color(color.getARGB(), true);
	}

	public void setStrokeColor(final reform.core.graphics.Color color) {
		_strokeColor = new Color(color.getARGB(), true);
	}

	public void reset() {
		_backgroundColor = null;
		_strokeColor = null;
		_path.reset();
	}

	public void draw(final Graphics2D g2)
	{
		if(_backgroundColor != null)
		{
			g2.setColor(_backgroundColor);
			g2.fill(_path);
		}
		g2.setColor(_strokeColor);
		g2.setStroke(_stroke);
		g2.draw(_path);
	}
}
