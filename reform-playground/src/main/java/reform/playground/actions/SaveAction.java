package reform.playground.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.json.JSONWriter;

import reform.core.project.Project;
import reform.identity.IdentifierEmitter;
import reform.playground.serializer.ProjectSerializer;
import reform.playground.serializer.SerializationError;

public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final Project _project;
	private final File _file;
	private final SaveAsAction _saveAs;
	private final IdentifierEmitter _idEmitter;

	public SaveAction(final Project project, final File file,
			final IdentifierEmitter idEmitter,
			final SaveAsAction saveAsAction) {
		_project = project;
		_file = file;
		_idEmitter = idEmitter;
		_saveAs = saveAsAction;

		putValue(NAME, "Save");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S',
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {

		if (_file == null) {
			_saveAs.actionPerformed(evt);
		} else {
			try {
				final ProjectSerializer serializer = new ProjectSerializer(
						_idEmitter);

				final PrintWriter textWriter = new PrintWriter(_file);
				final JSONWriter writer = new JSONWriter(textWriter);
				serializer.write(writer, _project);
				textWriter.flush();
			} catch (final FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error: File not found");
			} catch (final SerializationError e) {
				JOptionPane.showMessageDialog(null,
						String.format("Error while serializing Project. (%s)",
								e.getMessage()));
				e.printStackTrace();
			} catch (final Exception e) {
				JOptionPane.showMessageDialog(null, "Unexpected error");
				e.printStackTrace();
			}
		}

	}
}
