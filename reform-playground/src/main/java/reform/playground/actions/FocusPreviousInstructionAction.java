package reform.playground.actions;

import reform.core.procedure.RootInstruction;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FocusPreviousInstructionAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final InstructionFocus _focus;

	public FocusPreviousInstructionAction(final InstructionFocus focus)
	{
		_focus = focus;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{

		Instruction newFocus = _focus.getFocused();
		final InstructionGroup parent = newFocus.getParent();
		final int index = parent.indexOf(newFocus);
		if (index > 0)
		{
			newFocus = newFocus.getParent().get(index - 1);
		}
		else if (!(parent instanceof RootInstruction))
		{
			newFocus = parent.getParent().get(parent.getParent().indexOf(parent) - 1);
		}
		if (newFocus instanceof InstructionGroup)
		{
			final InstructionGroup newFocusGroup = (InstructionGroup) newFocus;
			newFocus = newFocusGroup.get(newFocusGroup.size() - 1);
		}

		_focus.setFocus(newFocus);
	}

}
