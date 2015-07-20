package reform.components.colorpicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class SaturationValueColorPlane extends JComponent
{
	private BufferedImage _overlay;

	private final ColorModel _model;

	private Color _color = Color.BLACK;

	public SaturationValueColorPlane(final ColorModel model)
	{
		_model = model;
		setFocusable(true);
		final MouseAdapter listener = new MouseAdapter()
		{


			@Override
			public void mousePressed(final MouseEvent e)
			{
				super.mousePressed(e);
				requestFocus();
				final double s = 1.0 * e.getX() / getWidth();
				final double v = 1 - 1.0 * e.getY() / getHeight();

				_model.setHSVA(_model.getHue(), Math.min(1, Math.max(0, s)), Math.min(1, Math.max(0, v)),
				               _model.getAlpha());
			}

			@Override
			public void mouseDragged(final MouseEvent e)
			{
				super.mouseDragged(e);
				final double s = 1.0 * e.getX() / getWidth();
				final double v = 1 - 1.0 * e.getY() / getHeight();

				_model.setHSVA(_model.getHue(), Math.min(1, Math.max(0, s)), Math.min(1, Math.max(0, v)),
				               _model.getAlpha());
			}
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
		model.addListener(this::onChange);

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(final ComponentEvent e)
			{
				_overlay = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

				final LinearGradientPaint horizontalGrad = new LinearGradientPaint(0, 0, getWidth(), 0,
				                                                                   new float[]{0, 1},
				                                                                   new Color[]{Color.WHITE, new Color(
						                                                                   0x00ffffff, true)});

				final LinearGradientPaint verticalGrad = new LinearGradientPaint(0, getHeight(), 0, 0,
				                                                                 new float[]{0, 1},
				                                                                 new Color[]{Color.BLACK, new Color(
						                                                                 0x00000000, true)});

				final Graphics2D g2 = (Graphics2D) _overlay.getGraphics();

				g2.setPaint(horizontalGrad);
				g2.fillRect(0, 0, getWidth(), getHeight());

				g2.setPaint(verticalGrad);
				g2.fillRect(0, 0, getWidth(), getHeight());

				g2.dispose();
			}
		});

		onChange(model);
	}

	private void onChange(final ColorModel colorModel)
	{
		_color = Color.getHSBColor((float) colorModel.getHue(), 1, 1);
		repaint();
	}

	public void paintComponent(final Graphics g)
	{
		final Graphics2D g2 = (Graphics2D) g;
		final int width = getWidth();
		final int height = getHeight();

		g2.setColor(_color);
		g2.fillRect(0, 0, width, height);

		g2.drawImage(_overlay, 0, 0, null);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
		g2.drawOval((int) (width * _model.getSaturation()) - 4, height - (int) (height * _model.getValue()) - 4, 8, 8);

		g2.setColor(Color.WHITE);
		g2.drawOval((int) (width * _model.getSaturation()) - 5, height - (int) (height * _model.getValue()) - 5, 10,
		            10);


	}

}
