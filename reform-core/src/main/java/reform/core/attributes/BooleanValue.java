package reform.core.attributes;

public class BooleanValue
{
	private boolean _value;

	public BooleanValue(boolean value)
	{
		_value = value;
	}

	public boolean getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _value;
	}

	public boolean getValue()
	{
		return _value;
	}

	public void setValue(boolean value)
	{
		_value = value;
	}
}
