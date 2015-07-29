package reform.core.attributes;

public class ConstantStringValue implements StringValue
{

	private String _string;

	public ConstantStringValue(String string) {
		_string = string;
	}

	@Override
	public String getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _string;
	}

	public String getString() {
		return _string;
	}

	public void setString(String color) {
		_string = color;
	}
}
