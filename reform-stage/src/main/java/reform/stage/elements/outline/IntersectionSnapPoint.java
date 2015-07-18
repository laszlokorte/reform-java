package reform.stage.elements.outline;

import reform.core.forms.Form;
import reform.core.forms.relations.IntersectionPoint;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.SnapPoint;

public class IntersectionSnapPoint implements SnapPoint {
	private final Identifier<? extends Form> _formIdB;
	private final Identifier<? extends Form> _formIdA;

	private final Vec2 _value = new Vec2();
	private final int _index;

	public IntersectionSnapPoint(final Identifier<? extends Form> formIdA,
			final Identifier<? extends Form> formIdB, final int index,
			final Vec2 pos) {
		_formIdA = formIdA;
		_formIdB = formIdB;
		_index = index;
		_value.set(pos);
	}

	@Override
	public double getX() {
		return _value.x;
	}

	@Override
	public double getY() {
		return _value.y;
	}

	public Identifier<? extends Form> getFormIdA() {
		return _formIdA;
	}

	public Identifier<? extends Form> getFormIdB() {
		return _formIdB;
	}

	@Override
	public ReferencePoint createReference() {
		return new IntersectionPoint(_index, _formIdA, _formIdB);
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

		return _formIdA.equals(other._formIdA)
				&& _formIdB.equals(other._formIdB) && _index == other._index;
	}

}
