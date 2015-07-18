package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.core.procedure.Procedure;
import reform.core.project.DataSet;
import reform.core.project.Picture;
import reform.evented.core.EventedProject;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.naming.Name;

public class NewPictureAction extends AbstractAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final EventedProject _project;

	private final IdentifierEmitter _idEmitter;

	public NewPictureAction(final EventedProject project,
			final IdentifierEmitter idEmitter) {
		_project = project;
		_idEmitter = idEmitter;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		_project.addPicture(new Picture(_idEmitter.emit(), new Name("Unnamed"),
				new Vec2i(400, 400), new DataSet(), new Procedure()));
	}

}
