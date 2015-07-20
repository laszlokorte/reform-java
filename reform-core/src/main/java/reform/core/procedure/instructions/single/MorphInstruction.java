package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.anchors.Anchor;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.TranslationDistance;
import reform.identity.Identifier;

public class MorphInstruction extends BaseInstruction
{

	private final Identifier<? extends Form> _formId;
	private final Identifier<? extends Anchor> _anchorId;
	private TranslationDistance _distance;

	public MorphInstruction(final Identifier<? extends Form> formId, final Identifier<? extends Anchor> anchorId,
	                        final TranslationDistance distance)
	{
		_formId = formId;
		_anchorId = anchorId;
		_distance = distance;
	}

	@Override
	public void evaluate(final Runtime runtime)
	{
		final Form form = runtime.get(_formId);
		if (form == null)
		{
			runtime.reportError(this, new Error("Form has not been initialized"));
		}
		else
		{
			final Anchor anchor = form.getAnchor(_anchorId);

			if (anchor == null)
			{
				runtime.reportError(this, new Error("Unknown anchor point"));
			}
			else
			{

				final double deltaX = _distance.getXValueForRuntime(runtime);
				final double deltaY = _distance.getYValueForRuntime(runtime);

				anchor.translate(runtime, deltaX, deltaY);
			}
		}
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		final Form form = analyzer.getForm(_formId);
		final String formName;
		final String anchorName;
		if (form != null)
		{
			formName = form.getName().getValue();
			final Anchor anchor = form.getAnchor(_anchorId);
			if (anchor != null)
			{
				anchorName = anchor.getName().getValue();
			}
			else
			{
				anchorName = "";
			}
		}
		else
		{
			formName = "???";
			anchorName = "";
		}

		analyzer.publish(this, "Move " + formName + "'s " + anchorName + " " + _distance.getDescription(analyzer));
	}

	@Override
	public Identifier<? extends Form> getTarget()
	{
		return _formId;
	}

	public void setDistance(final TranslationDistance distance)
	{
		_distance = distance;
	}

	public TranslationDistance getDistance()
	{
		return _distance;
	}

	public Identifier<? extends Form> getFormId()
	{
		return _formId;
	}

	public Identifier<? extends Anchor> getAnchorId()
	{
		return _anchorId;
	}
}
