package reform.playground.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import reform.playground.main.WindowBuilder;

public class CloseAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	public CloseAction(final WindowBuilder windowBuilder) {
		_windowBuilder = windowBuilder;

		putValue(NAME, "Close");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('W',
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		_windowBuilder.closeCurrentWindow();
	}

}
