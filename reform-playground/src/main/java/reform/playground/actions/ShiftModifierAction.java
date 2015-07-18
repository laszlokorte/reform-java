package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.stage.tooling.ToolController;

public class ShiftModifierAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private final ToolController _toolController;
	private final boolean _enable;

	public ShiftModifierAction(final ToolController toolController,
			final boolean enable) {
		_toolController = toolController;
		_enable = enable;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		_toolController.setShift(_enable);
	}

}
