package reform.playground.actions;

import reform.core.forms.Form;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.NullInstruction;
import reform.core.procedure.instructions.blocks.ForLoopInstruction;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class WrapInLoopAction extends AbstractAction implements InstructionFocus.Listener, EventedProcedure
		.Listener
{
	private static final long serialVersionUID = 1L;

	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	public WrapInLoopAction(final InstructionFocus focus, final EventedProcedure eProcedure)
	{
		_focus = focus;
		_eProcedure = eProcedure;

		putValue(Action.NAME, "Wrap in Loop");
		putValue(Action.SHORT_DESCRIPTION, "Wrap in Loop");

		_focus.addListener(this);
		_eProcedure.addListener(this);
		onFocusChanged(_focus);
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eProcedure.wrapInstruction(_focus.getFocused(), new ForLoopInstruction(10));
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus)
	{
		setEnabled(
				focus.isSet() && !(focus.getFocused() instanceof ForLoopInstruction) && !(focus.getFocused().getParent
						() instanceof ForLoopInstruction) && !(focus.getFocused() instanceof NullInstruction));
	}

	@Override
	public void onInstructionAdded(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		setEnabled(
				_focus.isSet() && !(_focus.getFocused() instanceof ForLoopInstruction) && !(_focus.getFocused()
						.getParent() instanceof ForLoopInstruction) && !(_focus.getFocused() instanceof
						NullInstruction));
	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		setEnabled(
				_focus.isSet() && !(_focus.getFocused() instanceof ForLoopInstruction) && !(_focus.getFocused()
						.getParent() instanceof ForLoopInstruction) && !(_focus.getFocused() instanceof
						NullInstruction));
	}

	@Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFormChanged(final EventedProcedure procedure, final Form form)
	{
		// TODO Auto-generated method stub

	}

}
