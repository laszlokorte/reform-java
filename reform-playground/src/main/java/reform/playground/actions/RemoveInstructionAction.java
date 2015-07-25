package reform.playground.actions;

import reform.evented.core.EventedProcedure;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class RemoveInstructionAction extends AbstractAction implements
		InstructionFocus.Listener
{
	private static final long serialVersionUID = 1L;

	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	public RemoveInstructionAction(final InstructionFocus focus, final EventedProcedure
			eProcedure)
	{
		_focus = focus;
		_eProcedure = eProcedure;

		putValue(NAME, "Remove Instruction");
		putValue(SHORT_DESCRIPTION, "Remove Instruction");
		focus.addListener(this);
		onFocusChanged(focus);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eProcedure.removeInstruction(_focus.getFocused());
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus)
	{
		setEnabled(focus.isSet() && _eProcedure.canRemoveInstruction(focus.getFocused
				()));
	}

}
