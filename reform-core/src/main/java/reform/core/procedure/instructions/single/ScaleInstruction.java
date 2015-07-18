package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.ScaleFactor;
import reform.identity.Identifier;

public class ScaleInstruction extends BaseInstruction {
	private final Identifier<? extends Form> _target;
	private final ScaleFactor _factor;
	private ReferencePoint _fixPoint;

	public ScaleInstruction(final Identifier<? extends Form> target,
			final ScaleFactor factor, final ReferencePoint fixPoint) {
		_target = target;
		_factor = factor;
		_fixPoint = fixPoint;
	}

	@Override
	public void evaluate(final Runtime runtime) {
		final Form form = runtime.get(_target);

		if (form == null) {
			runtime.reportError(this,
					new Error("Form has not been initialized"));
		} else if (!_fixPoint.isValidFor(runtime)) {
			runtime.reportError(this, new Error("Fixpoint is not defined"));
		} else if (!_factor.isValidFor(runtime)) {
			runtime.reportError(this, new Error("Factor is not defined"));
		} else {
			final double factor = _factor.getValueForRuntime(runtime);
			final double fixX = _fixPoint.getXValueForRuntime(runtime);
			final double fixY = _fixPoint.getYValueForRuntime(runtime);

			form.getScaler().scale(runtime, factor, fixX, fixY, 0, 0);
		}
	}

	@Override
	public void analyze(final Analyzer analyzer) {
		final Form form = analyzer.getForm(_target);
		String formName;
		if (form != null) {
			formName = form.getName().getValue();
		} else {
			formName = "???";
		}

		analyzer.publish(this,
				"Scale " + formName + " by " + _factor.getDescription(analyzer)
				+ " around " + _fixPoint.getDescription(analyzer));
	}

	@Override
	public Identifier<? extends Form> getTarget() {
		return _target;
	}

	public Identifier<? extends Form> getFormId() {
		return _target;
	}

	public ScaleFactor getFactor() {
		return _factor;
	}

	public ReferencePoint getFixPoint() {
		return _fixPoint;
	}

	public void setFixPoint(final ReferencePoint fixPoint) {
		_fixPoint = fixPoint;
	}

}
