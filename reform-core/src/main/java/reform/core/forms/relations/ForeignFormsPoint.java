package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;

public class ForeignFormsPoint implements ReferencePoint {

	private final Identifier<? extends ExposedPoint> _pointId;
	private final Identifier<? extends Form> _formId;

	public ForeignFormsPoint(final Identifier<? extends Form> formId,
			final Identifier<? extends ExposedPoint> pointId) {
		_formId = formId;
		_pointId = pointId;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime) {
		return runtime.get(_formId).getPoint(_pointId)
				.getXValueForRuntime(runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime) {
		return runtime.get(_formId).getPoint(_pointId)
				.getYValueForRuntime(runtime);
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		final Form form = analyzer.getForm(_formId);
		if (form == null) {
			return "???";
		}
		return form.getName().getValue() + "'s "
		+ form.getPoint(_pointId).getDescription(analyzer);
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		final Form form = runtime.get(_formId);
		return form != null && form.getPoint(_pointId) != null;
	}

	public Identifier<? extends Form> getFormId() {
		return _formId;
	}

	public Identifier<? extends ExposedPoint> getPointId() {
		return _pointId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final ForeignFormsPoint other = (ForeignFormsPoint) obj;

		return other._formId.equals(_formId) && other._pointId.equals(_pointId);
	}

}
