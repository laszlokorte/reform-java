package reform.stage.elements;

import reform.core.forms.Form;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.ForeignFormsPoint;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.math.Vec2;

public class EntityPoint implements SnapPoint {
	private final Identifier<? extends Form> _formId;
	private final Identifier<? extends ExposedPoint> _pointId;
	private final Vec2 _value = new Vec2();

	public <T extends Form> EntityPoint(final Identifier<T> formId,
			final ExposedPointToken<? super T> token) {
		_formId = formId;
		_pointId = new Identifier<>(token);
	}

	@Override
	public double getX() {
		return _value.x;
	}

	@Override
	public double getY() {
		return _value.y;
	}

	public Identifier<? extends Form> getFormId() {
		return _formId;
	}

	public Identifier<? extends ExposedPoint> getPointId() {
		return _pointId;
	}

	public void updateForRuntime(final Runtime runtime) {
		final ReferencePoint p = runtime.get(_formId).getPoint(_pointId);
		_value.set(p.getXValueForRuntime(runtime),
				p.getYValueForRuntime(runtime));
	}

	@Override
	public ReferencePoint createReference() {
		return new ForeignFormsPoint(getFormId(), getPointId());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EntityPoint other = (EntityPoint) obj;

		return _formId.equals(other._formId) && _pointId.equals(other._pointId);
	}
}
