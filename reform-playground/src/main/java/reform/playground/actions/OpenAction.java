package reform.playground.actions;

import org.json.JSONException;
import org.json.JSONObject;
import reform.core.project.Project;
import reform.identity.IdentifierEmitter;
import reform.playground.main.WindowBuilder;
import reform.playground.serializer.ProjectSerializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;

	public OpenAction(final WindowBuilder windowBuilder)
	{
		_windowBuilder = windowBuilder;

		putValue(NAME, "Open...");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		final JFileChooser fc = _windowBuilder.getFileChooser();

		SwingUtilities.invokeLater(() -> {
			final int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				final IdentifierEmitter idEmitter = new IdentifierEmitter(100);
				final ProjectSerializer serializer = new ProjectSerializer(idEmitter);

				try
				{
					final File file = fc.getSelectedFile();
					final Project project = serializer.read(new JSONObject(readFile(file.getPath(), Charset
							.defaultCharset())));

					_windowBuilder.open(file, project, idEmitter, false);
				} catch (final JSONException e)
				{
					JOptionPane.showMessageDialog(null, "JSON Error");
					e.printStackTrace();
				} catch (final IOException e)
				{
					JOptionPane.showMessageDialog(null, "IO Error");
				} catch (final Exception e)
				{
					JOptionPane.showMessageDialog(null, "Invalid file");
				}
			}
		});
	}

	private static String readFile(final String path, final Charset encoding) throws IOException
	{
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
