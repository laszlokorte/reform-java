package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;
import reform.identity.Identifier;

public class StaticLength implements Length {

	private final Identifier<? extends Form> _formId;
	private final int _offset;

	public StaticLength(final Identifier<? extends Form> formId,
			final int offset) {
		_formId = formId;
		_offset = offset;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime) {
		return Double.longBitsToDouble(runtime.get(_formId, _offset));
	}

	public void setForRuntime(final Runtime runtime, final double length) {
		runtime.set(_formId, _offset, Double.doubleToRawLongBits(length));
	}

	@Override
	public String getDescription(final Analyzer analyzer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return runtime.get(_formId) != null;
	}

}
