package reform.core.graphics;


import java.awt.*;
import java.awt.Color;
import java.awt.geom.GeneralPath;

public class ColoredShape
{
	private Stroke _stroke = new BasicStroke(1);
	private Color _backgroundColor;
	private Color _strokeColor;
	private final GeneralPath.Double _path = new GeneralPath.Double();

	public Stroke getStroke() {
		return _stroke;
	}

	public Color getBackgroundColor() {
		return _backgroundColor;
	}

	public GeneralPath.Double getPath() {
		return _path;
	}

	public void setBackgroundColor(final int color) {
		_backgroundColor = color != 0 ? new Color(color, true) : null;
	}

	public void setStrokeColor(final int color) {
		_strokeColor = color != 0 ? new Color(color, true) : null;
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
		if(_stroke != null && _strokeColor != null)
		{
			g2.setColor(_strokeColor);
			g2.setStroke(_stroke);
			g2.draw(_path);
		}
	}

	public void setStrokeWidth(final int strokeWidth)
	{
		_stroke = strokeWidth > 0 ? new BasicStroke(strokeWidth) : null;
	}
}
