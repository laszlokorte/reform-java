package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.TranslationDistance;
import reform.identity.Identifier;

public class TranslateInstruction extends BaseInstruction {
	private final Identifier<? extends Form> _target;
	private TranslationDistance _distance;

	public TranslateInstruction(final Identifier<? extends Form> target,
			final TranslationDistance distance) {

		_target = target;
		_distance = distance;
	}

	@Override
	public void evaluate(final Runtime runtime) {
		final Form form = runtime.get(_target);
		if (form == null) {
			runtime.reportError(this,
					new Error("Form has not been initialized"));
		} else if (!_distance.isValidFor(runtime)) {
			runtime.reportError(this, new Error("Distance is not defined"));
		} else {
			final double deltaX = _distance.getXValueForRuntime(runtime);
			final double deltaY = _distance.getYValueForRuntime(runtime);

			form.getTranslator().translate(runtime, deltaX, deltaY);
		}
	}

	@Override
	public void analyze(final Analyzer analyzer) {
		final Form form = analyzer.getForm(_target);
		final String formName;
		if (form != null) {
			formName = form.getName().getValue();
		} else {
			formName = "???";
		}

		analyzer.publish(this,
				"Move " + formName + " " + _distance.getDescription(analyzer));
	}

	@Override
	public Identifier<? extends Form> getTarget() {
		return _target;
	}

	public Identifier<? extends Form> getFormId() {
		return _target;
	}

	public TranslationDistance getDistance() {
		return _distance;
	}

	public void setDistance(final TranslationDistance distance) {
		_distance = distance;
	}

}
