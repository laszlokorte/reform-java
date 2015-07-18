package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.stage.tooling.ToolController;

public class ToggleToolOptionAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final ToolController _toolController;

	public ToggleToolOptionAction(final ToolController toolController) {
		_toolController = toolController;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		_toolController.toggle();
	}

}
