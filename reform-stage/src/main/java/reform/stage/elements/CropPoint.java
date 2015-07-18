package reform.stage.elements;

import reform.math.Vec2i;

public class CropPoint {
	private final Vec2i _position = new Vec2i();
	private final Vec2i _offset = new Vec2i();

	public CropPoint(final int x, final int y) {
		_offset.set(x, y);
	}

	public void updatePosition(final int width, final int height) {
		_position.set(width * (_offset.x + 1) / 2,
				height * (_offset.y + 1) / 2);
	}

	public double getX() {
		return _position.x;
	}

	public double getY() {
		return _position.y;
	}

	public int getOffsetX() {
		return _offset.x;
	}

	public int getOffsetY() {
		return _offset.y;
	}

	public boolean isCorner() {
		return _offset.x != 0 && _offset.y != 0;
	}
}