package reform.playground.actions;

import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolController;
import reform.stage.tooling.ToolControllerListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SelectToolAction extends AbstractAction implements ToolControllerListener
{
	private static final long serialVersionUID = 1L;

	private final ToolController _toolController;
	private final Tool _tool;

	public SelectToolAction(final ToolController toolController, final Tool tool, final String name)
	{
		_toolController = toolController;
		_tool = tool;

		putValue(Action.NAME, name);
		putValue(Action.SHORT_DESCRIPTION, name);
		toolController.addListener(this);
		setEnabled(!toolController.isActiveTool(_tool));
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_toolController.selectTool(_tool);
	}

	@Override
	public void onToolChange(final ToolController toolController)
	{
		setEnabled(!toolController.isActiveTool(_tool));
	}

}
