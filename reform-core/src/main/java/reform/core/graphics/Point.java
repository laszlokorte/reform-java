package reform.core.graphics;

import reform.math.Vec2;

public class Point
{
	private final Vec2 _pos = new Vec2();

	public Point(final double x, final double y)
	{
		_pos.set(x, y);
	}

	public Point(final Vec2 pos)
	{
		_pos.set(pos);
	}

	public double getX()
	{
		return _pos.x;
	}

	public double getY()
	{
		return _pos.y;
	}
}
