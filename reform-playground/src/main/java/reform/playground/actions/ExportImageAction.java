package reform.playground.actions;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import reform.core.graphics.ColoredShape;
import reform.stage.Stage;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolController;
import reform.stage.tooling.tools.PreviewTool;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;
import java.util.List;

public class ExportImageAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;

	private final Stage _stage;
	private final ToolController _toolController;
	private final PreviewTool _previewTool;

	private final JFileChooser _fileChooser;

	public ExportImageAction(final Stage stage, final ToolController toolController,
	                         final PreviewTool previewTool)
	{
		_stage = stage;
		_toolController = toolController;
		_previewTool = previewTool;
		_fileChooser = new JFileChooser();
		_fileChooser.setDialogTitle("Export...");

		final String[] formats = ImageIO.getWriterFormatNames();
		final HashSet<String> formatSet = new HashSet<>();
		for (int i = 0; i < formats.length; i++)
		{
			final String ext = formats[i].toLowerCase();
			if (formatSet.add(ext))
			{
				_fileChooser.addChoosableFileFilter(
						new FileNameExtensionFilter(ext, ext));
			}
		}

		_fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("svg", "svg"));


		putValue(NAME, "Export image...");
	}

	@Override
	public void actionPerformed(final ActionEvent evt)
	{
		final Tool prevTool = _toolController.getTool();
		_toolController.selectTool(_previewTool);

		SwingUtilities.invokeLater(() -> {
			final int returnVal = _fileChooser.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				final File selectedFile = _fileChooser.getSelectedFile();

				try
				{

					final String fileName = selectedFile.getName();
					final int lastDot = fileName.lastIndexOf(".");
					final String suffix;

					if (lastDot + 1 < fileName.length())
					{
						suffix = fileName.substring(lastDot + 1, fileName.length());
					}
					else
					{
						suffix = "png";
					}

					if (suffix.equals("svg"))
					{
						DOMImplementation domImpl = GenericDOMImplementation
								.getDOMImplementation();

						String svgNS = "http://www.w3.org/2000/svg";
						Document document = domImpl.createDocument(svgNS, "svg", null);

						SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
						svgGenerator.setSVGCanvasSize(
								new Dimension(_stage.getSize().x, _stage.getSize().y));


						draw(svgGenerator);

						Writer out = new OutputStreamWriter(
								new FileOutputStream(selectedFile), "UTF-8");
						svgGenerator.stream(out, true);
					}
					else
					{
						final BufferedImage image = new BufferedImage(_stage.getSize().x,
						                                              _stage.getSize().y,
						                                              BufferedImage
								                                              .TYPE_INT_RGB);

						final Graphics2D g2 = image.createGraphics();

						draw(g2);

						final ImageWriter writer = ImageIO.getImageWritersBySuffix(
								suffix).next();

						final ImageOutputStream stream = ImageIO.createImageOutputStream(
								selectedFile);
						writer.setOutput(stream);
						writer.write(image);
						writer.dispose();
						stream.close();
						g2.dispose();
					}

				} catch (final IOException ie)
				{
					ie.printStackTrace();
				} finally
				{
					_toolController.selectTool(prevTool);
				}

			}
			else
			{
				_toolController.selectTool(prevTool);

			}
		});
	}

	private void draw(final Graphics2D g2)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, _stage.getSize().x, _stage.getSize().y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                    RenderingHints.VALUE_ANTIALIAS_ON);

		final List<ColoredShape> finalShapes = _stage.getFinalShapes();
		for (int i = 0, j = finalShapes.size(); i < j; i++)
		{
			final ColoredShape s = finalShapes.get(i);

			s.draw(g2);
		}
	}

}
