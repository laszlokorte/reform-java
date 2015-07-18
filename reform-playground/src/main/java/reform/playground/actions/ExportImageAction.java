package reform.playground.actions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import reform.stage.Stage;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolController;
import reform.stage.tooling.tools.PreviewTool;

public class ExportImageAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final Stage _stage;
	private final ToolController _toolController;
	private final PreviewTool _previewTool;

	private final JFileChooser _fileChooser;

	public ExportImageAction(final Stage stage,
			final ToolController toolController,
			final PreviewTool previewTool) {
		_stage = stage;
		_toolController = toolController;
		_previewTool = previewTool;
		_fileChooser = new JFileChooser();
		_fileChooser.setDialogTitle("Export...");

		final String[] formats = ImageIO.getWriterFormatNames();
		for (int i = 0; i < formats.length; i++) {
			_fileChooser.addChoosableFileFilter(
					new FileNameExtensionFilter(formats[i], formats[i]));
		}

		putValue(NAME, "Export image...");
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final Tool prevTool = _toolController.getTool();
		_toolController.selectTool(_previewTool);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final int returnVal = _fileChooser.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File selectedFile = _fileChooser.getSelectedFile();

					try {

						final BufferedImage image = new BufferedImage(
								_stage.getSize().x, _stage.getSize().y,
								BufferedImage.TYPE_INT_RGB);

						final Graphics2D g2 = image.createGraphics();

						g2.setColor(Color.WHITE);
						g2.fillRect(0, 0, _stage.getSize().x,
								_stage.getSize().y);
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);

						final Color strokeColor = Color.BLACK;
						final Color fillColor = new Color(0x99222222, true);
						final List<Shape> finalShapes = _stage.getFinalShapes();
						for (int i = 0, j = finalShapes.size(); i < j; i++) {
							final Shape s = finalShapes.get(i);

							g2.setColor(strokeColor);
							g2.draw(s);
							g2.setColor(fillColor);
							g2.fill(s);
						}

						final String fileName = selectedFile.getName();
						final int lastDot = fileName.lastIndexOf(".");
						final String suffix;

						if (lastDot + 1 < fileName.length()) {
							suffix = fileName.substring(lastDot + 1,
									fileName.length());
						} else {
							suffix = "png";
						}

						final ImageWriter writer = ImageIO
								.getImageWritersBySuffix(suffix).next();

						final ImageOutputStream stream = ImageIO
								.createImageOutputStream(selectedFile);
						writer.setOutput(stream);
						writer.write(image);
						writer.dispose();
						stream.close();
						g2.dispose();

					} catch (final IOException ie) {
						ie.printStackTrace();
					} finally {
						_toolController.selectTool(prevTool);
					}

				} else {
					_toolController.selectTool(prevTool);

				}
			}
		});
	}

}
