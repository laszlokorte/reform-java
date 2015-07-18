package reform.stage.elements;

import reform.core.forms.Form;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.ForeignFormsPoint;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vec2;

public class Handle {
	private final Identifier<? extends Form> _formId;
	private final Identifier<? extends ExposedPoint> _pointId;
	private final Identifier<? extends Anchor> _anchorId;
	private final Vec2 _value = new Vec2();

	private final PivotPair _pivot;

	public <T extends Form> Handle(final Identifier<T> formId,
			final ExposedPointToken<? super T> pointToken,
			final IdentityToken anchorToken, final PivotPair pivot) {
		_formId = formId;
		_pointId = new Identifier<>(pointToken);
		_anchorId = new Identifier<>(anchorToken);
		_pivot = pivot;
	}

	public <T extends Form> Handle(final Identifier<T> formId,
			final ExposedPointToken<? super T> pointToken,
			final IdentityToken anchorToken) {
		this(formId, pointToken, anchorToken, null);
	}

	public double getX() {
		return _value.x;
	}

	public double getY() {
		return _value.y;
	}

	public Identifier<? extends Anchor> getAnchorId() {
		return _anchorId;
	}

	public Identifier<? extends Form> getFormId() {
		return _formId;
	}

	public void updateForRuntime(final Runtime runtime) {
		final ReferencePoint p = runtime.get(_formId).getPoint(_pointId);
		_value.set(p.getXValueForRuntime(runtime),
				p.getYValueForRuntime(runtime));
	}

	public PivotPair getPivot() {
		return _pivot;
	}

	public boolean hasPivot() {
		return _pivot != null;
	}

	public ReferencePoint createReference() {
		return new ForeignFormsPoint(_formId, _pointId);
	}

}