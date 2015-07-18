package reform.core.procedure.instructions.single;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.instructions.BaseInstruction;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.RotationAngle;
import reform.identity.Identifier;

public class RotateInstruction extends BaseInstruction {
	private final Identifier<? extends Form> _target;
	private final RotationAngle _angle;
	private ReferencePoint _fixPoint;

	public RotateInstruction(final Identifier<? extends Form> target,
			final RotationAngle angle, final ReferencePoint fixPoint) {
		_target = target;
		_angle = angle;
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
		} else if (!_angle.isValidFor(runtime)) {
			runtime.reportError(this, new Error("Angle is not defined"));
		} else {
			final double fixX = _fixPoint.getXValueForRuntime(runtime);
			final double fixY = _fixPoint.getYValueForRuntime(runtime);
			final double angle = _angle.getValueForRuntime(runtime);
			form.getRotator().rotate(runtime, angle, fixX, fixY);
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
				"Rotate " + formName + " by " + _angle.getDescription(analyzer)
						+ " around " + _fixPoint.getDescription(analyzer));
	}

	@Override
	public Identifier<? extends Form> getTarget() {
		return _target;
	}

	public Identifier<? extends Form> getFormId() {
		return _target;
	}

	public RotationAngle getAngle() {
		return _angle;
	}

	public ReferencePoint getFixPoint() {
		return _fixPoint;
	}

	public void setFixPoint(final ReferencePoint fixPoint) {
		_fixPoint = fixPoint;
	}
}
