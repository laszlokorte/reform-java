package reform.playground.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import reform.core.project.Project;
import reform.identity.IdentifierEmitter;
import reform.playground.main.WindowBuilder;
import reform.playground.serializer.ProjectSerializer;

public class OpenExampleAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	public OpenExampleAction(final WindowBuilder windowBuilder) {
		_windowBuilder = windowBuilder;

		putValue(NAME, "Open Example");
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke('O',
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
								| InputEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("JSON", "json"));

		final IdentifierEmitter idEmitter = new IdentifierEmitter(100);
		final ProjectSerializer serializer = new ProjectSerializer(idEmitter);

		try {

			final Project project = serializer.read(new JSONObject(
					new JSONTokener(getClass().getResourceAsStream(
							"/new.json"))));

			_windowBuilder.open(null, project, idEmitter, false);
		} catch (final JSONException e) {
			JOptionPane.showMessageDialog(null, "JSON Error");
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Invalid file");
		}
	}

}
