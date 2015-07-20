package reform.playground.actions;

import reform.core.procedure.instructions.NullInstruction;
import reform.core.procedure.instructions.blocks.IfConditionInstruction;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class WrapInIfAction extends AbstractAction implements InstructionFocus.Listener
{

	private static final long serialVersionUID = 1L;
	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	public WrapInIfAction(final InstructionFocus focus, final EventedProcedure eProcedure)
	{
		_focus = focus;
		_eProcedure = eProcedure;

		putValue(Action.NAME, "Wrap in Conditional");
		putValue(Action.SHORT_DESCRIPTION, "Wrap in Conditional");

		_focus.addListener(this);
		onFocusChanged(_focus);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eProcedure.wrapInstruction(_focus.getFocused(), new IfConditionInstruction());
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus)
	{
		setEnabled(focus.isSet() && !(focus.getFocused() instanceof NullInstruction));
	}

}
