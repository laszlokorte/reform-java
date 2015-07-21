package reform.stage.elements.errors;

import reform.core.forms.relations.RelativeDynamicSizeDestination;
import reform.core.runtime.relations.InitialDestination;
import reform.math.Vec2;

public class DynamicSizeDestinationMarker implements Marker
{
	private final Vec2 _position = new Vec2();
	private final RelativeDynamicSizeDestination _destination;

	public DynamicSizeDestinationMarker(RelativeDynamicSizeDestination destination, Vec2 position) {
		_destination = destination;
		_position.set(position);
	}

	public Vec2 getPosition() {
		return _position;
	}

	public void setPosition(Vec2 newPosition) {
		_position.set(newPosition);
	}
}
