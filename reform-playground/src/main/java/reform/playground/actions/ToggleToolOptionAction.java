package reform.playground.actions;

import reform.stage.tooling.ToolController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ToggleToolOptionAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final ToolController _toolController;

	public ToggleToolOptionAction(final ToolController toolController)
	{
		_toolController = toolController;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_toolController.toggle();
	}

}
