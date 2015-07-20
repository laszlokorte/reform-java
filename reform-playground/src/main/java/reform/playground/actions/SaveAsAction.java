package reform.playground.actions;

import org.json.JSONWriter;
import reform.core.project.Project;
import reform.identity.IdentifierEmitter;
import reform.playground.main.WindowBuilder;
import reform.playground.serializer.ProjectSerializer;
import reform.playground.serializer.SerializationError;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveAsAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final WindowBuilder _windowBuilder;
	private final Project _project;
	private File _file;

	private final IdentifierEmitter _idEmitter;

	public SaveAsAction(final WindowBuilder windowBuilder, final Project project, final File file, final
	IdentifierEmitter idEmitter)
	{
		_windowBuilder = windowBuilder;
		_project = project;
		_file = file;
		_idEmitter = idEmitter;

		putValue(NAME, "Save As...");
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		final JFileChooser fc = _windowBuilder.getFileChooser();
		if (_file != null)
		{
			fc.setCurrentDirectory(_file);
		}
		fc.setFileFilter(new FileNameExtensionFilter("JSON", "json"));

		SwingUtilities.invokeLater(() -> {
			final int returnVal = fc.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				final ProjectSerializer serializer = new ProjectSerializer(_idEmitter);

				final JSONWriter writer;
				try
				{
					final File selectedFile = fc.getSelectedFile();
					final PrintWriter textWriter = new PrintWriter(selectedFile);
					writer = new JSONWriter(textWriter);
					serializer.write(writer, _project);
					textWriter.flush();
					_file = selectedFile;
				} catch (final FileNotFoundException e)
				{
					JOptionPane.showMessageDialog(null, "Error: File not found");
				} catch (final SerializationError e)
				{
					JOptionPane.showMessageDialog(null, String.format("Error while serializing Project. (%s)", e
							.getMessage()));
					e.printStackTrace();
				} catch (final Exception e)
				{
					JOptionPane.showMessageDialog(null, "Unexpected error");
					e.printStackTrace();
				}

			}
		});
	}

}
