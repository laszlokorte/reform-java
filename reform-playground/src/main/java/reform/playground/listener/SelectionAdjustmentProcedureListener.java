package reform.playground.listener;

import reform.core.forms.Form;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.single.CreateFormInstruction;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.InstructionFocus;

public class SelectionAdjustmentProcedureListener
		implements EventedProcedure.Listener {

	private final FormSelection _formSelection;
	private int _index = -1;

	public SelectionAdjustmentProcedureListener(final FormSelection formSelection) {
        _formSelection = formSelection;
	}

	@Override
	public void onInstructionAdded(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {

	}

    @Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		if(instruction instanceof CreateFormInstruction) {
            CreateFormInstruction i = (CreateFormInstruction) instruction;
            if(i.getForm().getId().equals(_formSelection.getSelected())) {
                _formSelection.reset();
            }
        }
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
