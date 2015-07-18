package reform.core.forms.relations;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Length;

public class ScaledLength implements Length {

	private final Length _length;
	private final double _factor;

	public ScaledLength(final Length length, final double factor) {
		_length = length;
		_factor = factor;
	}

	@Override
	public double getValueForRuntime(final Runtime runtime) {
		return _length.getValueForRuntime(runtime) * _factor;
	}

	@Override
	public boolean isValidFor(final Runtime runtime) {
		return _length.isValidFor(runtime);
	}
}
