package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.outline.Intersector;
import reform.core.forms.outline.Outline;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;

public class IntersectionPoint implements ReferencePoint
{

	private final int _index;
	private final Identifier<? extends Form> _formAId;
	private final Identifier<? extends Form> _formBId;

	public IntersectionPoint(final int index, final Identifier<? extends Form> formAId,
	                         final Identifier<? extends
			Form> formBId)
	{
		_index = index;
		_formAId = formAId;
		_formBId = formBId;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		final Form a = runtime.get(_formAId);
		final Form b = runtime.get(_formBId);

		if (a == null || b == null)
		{
			return false;
		}

		final Outline outlineA = a.getOutline();
		final Outline outlineB = b.getOutline();

		return !Double.isNaN(
				Intersector.intersectXForRuntime(runtime, outlineA, outlineB, _index));
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		final Outline outlineA = runtime.get(_formAId).getOutline();
		final Outline outlineB = runtime.get(_formBId).getOutline();

		return Intersector.intersectXForRuntime(runtime, outlineA, outlineB, _index);

	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		final Outline outlineA = runtime.get(_formAId).getOutline();
		final Outline outlineB = runtime.get(_formBId).getOutline();

		return Intersector.intersectYForRuntime(runtime, outlineA, outlineB, _index);
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		final Form formA = analyzer.getForm(_formAId);
		final Form formB = analyzer.getForm(_formBId);

		return "Intersection#" + (_index + 1) + " of " + (formA != null ? formA.getName
				().getValue() : "???") + " " +
				"and" +
				" " +
				"" + (formB != null ? formB.getName().getValue() : "???");
	}

	public int getIndex()
	{
		return _index;
	}

	public Identifier<? extends Form> getFormAId()
	{
		return _formAId;
	}

	public Identifier<? extends Form> getFormBId()
	{
		return _formBId;
	}

}
