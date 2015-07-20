package reform.stage.elements;

import reform.math.Vec2i;
import reform.math.Vector;

public class CropPoint
{
	public static final double GRAB_RADIUS = 10;
	private static final double GRAB_RADIUS2 = GRAB_RADIUS * GRAB_RADIUS;

	private final Vec2i _position = new Vec2i();
	private final Vec2i _offset = new Vec2i();

	public CropPoint(final int x, final int y)
	{
		_offset.set(x, y);
	}

	public void updatePosition(final int width, final int height)
	{
		_position.set(width * (_offset.x + 1) / 2, height * (_offset.y + 1) / 2);
	}

	public double getX()
	{
		return _position.x;
	}

	public double getY()
	{
		return _position.y;
	}

	public int getOffsetX()
	{
		return _offset.x;
	}

	public int getOffsetY()
	{
		return _offset.y;
	}

	public boolean isCorner()
	{
		return _offset.x != 0 && _offset.y != 0;
	}

	public boolean isInGrabRadius(final double x, final double y)
	{
		return Vector.distance2(x, y, _position.x, _position.y) < GRAB_RADIUS2;
	}
}
