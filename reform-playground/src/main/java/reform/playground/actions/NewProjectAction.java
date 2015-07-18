package reform.playground.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import reform.core.procedure.Procedure;
import reform.core.project.DataSet;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.naming.Name;
import reform.playground.main.WindowBuilder;

public class NewProjectAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	private final IdentifierEmitter _idEmitter;

	public NewProjectAction(final WindowBuilder windowBuilder,
			final IdentifierEmitter idEmitter) {
		_windowBuilder = windowBuilder;
		_idEmitter = idEmitter;

		putValue(NAME, "New");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('N',
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Project project = new Project();

		project.addPicture(new Picture(_idEmitter.emit(), new Name("Unnamed"),
				new Vec2i(500, 350), new DataSet(), new Procedure()));

		_windowBuilder.open(null, project, _idEmitter, false);
	}

}