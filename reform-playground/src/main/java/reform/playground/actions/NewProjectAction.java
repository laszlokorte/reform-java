package reform.playground.actions;

import reform.core.procedure.Procedure;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.data.sheet.Sheet;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.naming.Name;
import reform.playground.main.WindowBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NewProjectAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	private final IdentifierEmitter _idEmitter;

	public NewProjectAction(final WindowBuilder windowBuilder, final IdentifierEmitter idEmitter)
	{
		_windowBuilder = windowBuilder;
		_idEmitter = idEmitter;

		putValue(NAME, "New");
		putValue(Action.ACCELERATOR_KEY,
		         KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		final Project project = new Project();

		project.addPicture(
				new Picture(_idEmitter.emit(), new Name("Unnamed"), new Vec2i(500, 350), new Sheet(), new Procedure(),
				            new Sheet()));

		_windowBuilder.open(null, project, _idEmitter, false);
	}

}
