package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.InvalidDestinationError;
import reform.core.runtime.relations.InitialDestination;
import reform.identity.Identifier;

public class CreateFormInstruction extends BaseInstruction
{
	private final Form _form;
	private InitialDestination _destination;

	public CreateFormInstruction(final Form form, final InitialDestination destination)
	{
		_form = form;
		_destination = destination;
	}

	@Override
	public void evaluate(final Runtime runtime)
	{
		if (!_destination.isValidFor(runtime))
		{
			runtime.reportError(this, new InvalidDestinationError(_destination));
		}
		else
		{
			final double minX = _destination.getMinXForRuntime(runtime);
			final double minY = _destination.getMinYForRuntime(runtime);
			final double maxX = _destination.getMaxXForRuntime(runtime);
			final double maxY = _destination.getMaxYForRuntime(runtime);

			runtime.declare(_form);

			_form.initialize(runtime, minX, minY, maxX, maxY);
		}
	}

	@Override
	public void analyze(final Analyzer analyzer)
	{
		analyzer.announceForm(_form);
		analyzer.publish(this, "Create " + _form.getName().getValue() + " " + _destination.getDescription(analyzer));
	}

	@Override
	public Identifier<? extends Form> getTarget()
	{
		return _form.getId();
	}

	public Form getForm()
	{
		return _form;
	}

	public InitialDestination getDestination()
	{
		return _destination;
	}

	public void setDestination(final InitialDestination destination)
	{
		_destination = destination;
	}
}
