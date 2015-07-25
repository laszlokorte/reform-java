package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;

public class StaticPoint implements ReferencePoint
{

	private final Identifier<? extends Form> _formId;
	private final int _offset;

	public StaticPoint(final Identifier<? extends Form> formId, final int offset)
	{
		_formId = formId;
		_offset = offset;
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return Double.longBitsToDouble(runtime.get(_formId, _offset));
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return Double.longBitsToDouble(runtime.get(_formId, _offset + 1));
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setForRuntime(final Runtime runtime, final double x, final double y)
	{
		runtime.set(_formId, _offset, Double.doubleToRawLongBits(x));
		runtime.set(_formId, _offset + 1, Double.doubleToRawLongBits(y));
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return true;
	}

}
