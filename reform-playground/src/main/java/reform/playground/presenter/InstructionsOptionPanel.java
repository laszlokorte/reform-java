package reform.playground.presenter;

import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import reform.core.forms.Form;
import reform.core.forms.relations.ConstantAngle;
import reform.core.forms.relations.ConstantScaleFactor;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.blocks.ForLoopInstruction;
import reform.core.procedure.instructions.blocks.IfConditionInstruction;
import reform.core.procedure.instructions.single.RotateInstruction;
import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.core.runtime.relations.RotationAngle;
import reform.core.runtime.relations.ScaleFactor;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.InstructionFocus;

public final class InstructionsOptionPanel implements InstructionFocus.Listener,
		ChangeListener, EventedProcedure.Listener {
	private final JPanel _panel = new JPanel(
			new FlowLayout(FlowLayout.LEFT, 0, 0));
	private final JLabel _label = new JLabel("T:");

	private final SpinnerModel _intModel = new SpinnerNumberModel(1, 0, 100, 1);
	private final SpinnerModel _percentModel = new SpinnerNumberModel(1.0,
			-100.0, 100.0, 1.0);
	private final SpinnerModel _freeModel = new SpinnerNumberModel(1.0, -1000,
			1000.0, 1.0);
	private final JSpinner _spinner = new JSpinner(_intModel);
	private final JCheckBox _checkbox = new JCheckBox();

	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	public InstructionsOptionPanel(final EventedProcedure eProcedure,
			final InstructionFocus focus) {
		_eProcedure = eProcedure;
		_focus = focus;
		_panel.add(_label);
		_panel.add(_spinner);
		_panel.add(_checkbox);

		_spinner.addChangeListener(this);
		_checkbox.addChangeListener(this);

		focus.addListener(this);
		onFocusChanged(focus);

		eProcedure.addListener(this);

	}

	public Component getComponent() {
		return _panel;
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus) {
		boolean showSpinner = false;
		boolean showCheckbox = false;

		if (focus.isSet()) {
			if (focus.getFocused() instanceof ForLoopInstruction) {
				final ForLoopInstruction loop = (ForLoopInstruction) focus
						.getFocused();
				setSpinnerModel(_intModel, 0);
				_label.setText("T:");
				_spinner.setValue(loop.getTimes());
				showSpinner = true;
			} else if (focus.getFocused() instanceof ScaleInstruction) {
				final ScaleInstruction instruction = (ScaleInstruction) focus
						.getFocused();
				final ScaleFactor factor = instruction.getFactor();
				if (factor instanceof ConstantScaleFactor) {
					final ConstantScaleFactor f = (ConstantScaleFactor) factor;
					setSpinnerModel(_freeModel, 1);
					_label.setText("S:");
					_spinner.setValue(f.getValue() * 100);
					showSpinner = true;

				}
			} else if (focus.getFocused() instanceof RotateInstruction) {
				final RotateInstruction instruction = (RotateInstruction) focus
						.getFocused();
				final RotationAngle angle = instruction.getAngle();
				if (angle instanceof ConstantAngle) {
					final ConstantAngle a = (ConstantAngle) angle;
					setSpinnerModel(_percentModel, 1);
					_label.setText("R:");
					_spinner.setValue(a.getValue() * 50 / Math.PI);
					showSpinner = true;

				}
			} else if (focus.getFocused() instanceof IfConditionInstruction) {
				final IfConditionInstruction instruction = (IfConditionInstruction) focus
						.getFocused();
				final boolean condition = instruction.getCondition();
				_label.setText("C:");
				_checkbox.setSelected(condition);
				showCheckbox = true;
			}
		}

		_checkbox.setVisible(showCheckbox);
		_spinner.setVisible(showSpinner);
		_label.setVisible(showCheckbox || showSpinner);
	}

	private void setSpinnerModel(final SpinnerModel model, final int digits) {
		_spinner.setModel(model);
		final JSpinner.NumberEditor editor = (JSpinner.NumberEditor) _spinner
				.getEditor();
		final DecimalFormat format = editor.getFormat();
		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);

		final JFormattedTextField txt = editor.getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
	}

	@Override
	public void stateChanged(final ChangeEvent e) {

		if (_focus.isSet()) {
			if (_focus.getFocused() instanceof ForLoopInstruction) {
				final ForLoopInstruction loop = (ForLoopInstruction) _focus
						.getFocused();
				loop.setTimes((Integer) _spinner.getValue());
				_eProcedure.publishInstructionChange(loop);
			} else if (_focus.getFocused() instanceof ScaleInstruction) {
				final ScaleInstruction instruction = (ScaleInstruction) _focus
						.getFocused();
				final ScaleFactor factor = instruction.getFactor();
				if (factor instanceof ConstantScaleFactor) {
					final ConstantScaleFactor f = (ConstantScaleFactor) factor;
					f.setFactor((Double) _spinner.getValue() / 100.0);
					_eProcedure.publishInstructionChange(instruction);

				}

			} else if (_focus.getFocused() instanceof RotateInstruction) {
				final RotateInstruction instruction = (RotateInstruction) _focus
						.getFocused();
				final RotationAngle angle = instruction.getAngle();
				if (angle instanceof ConstantAngle) {
					final ConstantAngle a = (ConstantAngle) angle;
					a.setAngle(
							(Double) _spinner.getValue() * Math.PI * 2 / 100);
					_eProcedure.publishInstructionChange(instruction);

				}
			} else if (_focus.getFocused() instanceof IfConditionInstruction) {
				final IfConditionInstruction instruction = (IfConditionInstruction) _focus
						.getFocused();
				instruction.setCondition(_checkbox.isSelected());
				_eProcedure.publishInstructionChange(instruction);
			}
		}
	}

	@Override
	public void onInstructionAdded(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

    @Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		if (_focus.isSet() && _focus.getFocused() == instruction) {
			onFocusChanged(_focus);
		}
	}

	@Override
	public void onFormChanged(final EventedProcedure procedure,
			final Form form) {
		// TODO Auto-generated method stub

	}
}
