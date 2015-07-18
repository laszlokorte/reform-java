package reform.core.graphics;

public class Stroke {
	final private double _width;
	final private Color _color;

	public Stroke(final double width, final Color color) {
		_width = width;
		_color = color;
	}

	public double getWidth() {
		return _width;
	}

	public Color getColor() {
		return _color;
	}
}
