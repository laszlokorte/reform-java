package reform.stage.elements.errors;

import reform.core.forms.relations.RelativeDistance;
import reform.math.Vec2;

public class RelativeDistanceMarker implements Marker
{
	private final Vec2 _position = new Vec2();
	private final RelativeDistance _distance;

	public RelativeDistanceMarker(RelativeDistance distance, Vec2 position) {
		_distance = distance;
		_position.set(position);
	}

	public Vec2 getPosition() {
		return _position;
	}

	public void setPosition(Vec2 newPosition) {
		_position.set(newPosition);
	}
}
