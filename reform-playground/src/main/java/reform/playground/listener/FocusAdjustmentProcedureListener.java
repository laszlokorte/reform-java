package reform.playground.listener;

import reform.core.forms.Form;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.InstructionFocus;

public class FocusAdjustmentProcedureListener
		implements EventedProcedure.Listener {

	private final InstructionFocus _focus;
	private int _index = -1;

	public FocusAdjustmentProcedureListener(final InstructionFocus focus) {
		_focus = focus;
	}

	@Override
	public void onInstructionAdded(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		if (_focus.isFocused(instruction)) {
			_focus.setFocus(parent.get(_index - 1));
			_index = -1;
		}
	}

    @Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		_index = parent.indexOf(instruction);
	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFormChanged(final EventedProcedure procedure,
			final Form form) {
		// TODO Auto-generated method stub

	}

}
