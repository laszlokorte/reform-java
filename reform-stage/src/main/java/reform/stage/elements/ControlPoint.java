package reform.stage.elements;


import reform.math.Vec2;

public class ControlPoint
{
	private final Vec2 _position = new Vec2();
	private boolean _hasError = false;

	public double getX() {
		return _position.x;
	}

	public double getY() {
		return _position.y;
	}

	public boolean hasError() {
		return _hasError;
	}

	public void updatePosition(double x, double y) {
		_position.set(x,y);
	}

	public void setError(final boolean error)
	{
		_hasError = error;
	}
}
