package reform.core.attributes;

import reform.core.runtime.*;

public class ConstantColorValue implements ColorValue
{

	private int _color= 0;

	@Override
	public int getValueForRuntime(final reform.core.runtime.Runtime runtime)
	{
		return _color;
	}

	public int getColor() {
		return _color;
	}

	public void setColor(int color) {
		_color = color;
	}
}
