package reform.playground.actions;

import reform.playground.main.WindowBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	public CloseAction(final WindowBuilder windowBuilder)
	{
		_windowBuilder = windowBuilder;

		putValue(NAME, "Close");
		putValue(Action.ACCELERATOR_KEY,
		         KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		_windowBuilder.closeCurrentWindow();
	}

}
