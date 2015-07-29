package reform.core.attributes;


import reform.core.runtime.*;
import reform.identity.Identifier;

public class ConstantScalarValue implements ScalarValue
{
	private double _scalar;

	public ConstantScalarValue(double scalar) {
		_scalar = scalar;
	}

	@Override
	public double getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _scalar;
	}

	public double getValue() {
		return _scalar;
	}

	public void setValue(double value) {
		_scalar = value;
	}
}
