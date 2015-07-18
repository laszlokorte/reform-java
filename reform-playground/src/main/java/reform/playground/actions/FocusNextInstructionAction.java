package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.core.procedure.RootInstruction;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.stage.tooling.InstructionFocus;

public class FocusNextInstructionAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final InstructionFocus _focus;

	public FocusNextInstructionAction(final InstructionFocus focus) {
		_focus = focus;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {

		Instruction newFocus = _focus.getFocused();
		final InstructionGroup parent = newFocus.getParent();
		final int index = parent.indexOf(newFocus);

		if (newFocus instanceof InstructionGroup) {
			final InstructionGroup group = (InstructionGroup) newFocus;
			if (group.size() > 0) {
				newFocus = group.get(0);
			}
		} else if (index < parent.size() - 1) {
			newFocus = parent.get(index + 1);
		} else if (!(parent instanceof RootInstruction)) {
			newFocus = parent.getParent()
					.get(parent.getParent().indexOf(parent) + 1);
		}

		if (newFocus instanceof InstructionGroup) {
			newFocus = ((InstructionGroup) newFocus).get(0);
		}

		_focus.setFocus(newFocus);
	}

}
