package reform.stage.elements.outline;

import reform.core.forms.Form;
import reform.core.forms.relations.IntersectionPoint;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vector;
import reform.stage.elements.Entity;
import reform.stage.elements.SnapPoint;

public class IntersectionSnapPoint implements SnapPoint {
    private static final double SNAP_RADIUS2 = SnapPoint
            .SNAP_RADIUS*SnapPoint.SNAP_RADIUS;

    Entity _entityA;
    Entity _entityB;

    Vec2 _position = new Vec2();
    int _index;

    IntersectionSnapPoint() {
    }

    void reset(final Entity entityA,
                                 final Entity entityB, final int index,
                                 final Vec2 pos) {
        _entityA = entityA;
        _entityB = entityB;
        _index = index;
        _position.set(pos);
    }

    @Override
	public double getX() {
		return _position.x;
	}

	@Override
	public double getY() {
		return _position.y;
	}

	public Identifier<? extends Form> getFormIdA() {
		return _entityA.getId();
	}

	public Identifier<? extends Form> getFormIdB() {
		return _entityB.getId();
	}

	@Override
	public ReferencePoint createReference() {
		return new IntersectionPoint(_index, _entityA.getId(), _entityB.getId
                ());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IntersectionSnapPoint other = (IntersectionSnapPoint) obj;

		return _entityA.getId().equals(other._entityA.getId())
				&& _entityB.getId().equals(other._entityB.getId()) && _index
                == other
                ._index;
	}

    public String getLabel() {
        return "Intersection of " + _entityA.getLabel() + " and " + _entityB
                .getLabel();
    }

    public boolean isInSnapRadius(double x, double y) {
        return Vector.distance2(x, y, _position.x, _position.y) < SNAP_RADIUS2;
    }

}
