package reform.playground.actions;

import reform.core.procedure.Procedure;
import reform.core.project.Picture;
import reform.data.sheet.Sheet;
import reform.evented.core.EventedProject;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.naming.Name;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewPictureAction extends AbstractAction
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final EventedProject _project;

	private final IdentifierEmitter _idEmitter;

	public NewPictureAction(final EventedProject project, final IdentifierEmitter idEmitter)
	{
		_project = project;
		_idEmitter = idEmitter;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_project.addPicture(new Picture(_idEmitter.emit(), new Name("Unnamed"), new Vec2i(400, 400), new Sheet(),
		                                new Procedure(), new Sheet()));
	}

}
