package reform.components.colorpicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class AlphaColorTrack extends JComponent
{
	private final Composite _composite = AlphaComposite.getInstance(AlphaComposite
			                                                                .DST_IN, 1.0F);

	private final ColorModel _model;
	private BufferedImage _overlay;
	private Color _color = Color.BLACK;

	public AlphaColorTrack(final ColorModel model)
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
				final double alpha = 1.0 * e.getX() / getWidth();

				_model.setAlpha(Math.min(1, Math.max(0, alpha)));
			}

			@Override
			public void mouseDragged(final MouseEvent e)
			{
				super.mouseDragged(e);
				final double alpha = 1.0 * e.getX() / getWidth();

				_model.setAlpha(Math.min(1, Math.max(0, alpha)));
			}
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
		model.addListener(this::onChange);

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(final ComponentEvent e)
			{
				_overlay = new BufferedImage(getWidth(), getHeight(),
				                             BufferedImage.TYPE_4BYTE_ABGR);

				final LinearGradientPaint horizontalGrad = new LinearGradientPaint(0, 0,
				                                                                   getWidth(),
				                                                                   0,
				                                                                   new
						                                                                   float[]{0, 1},
				                                                                   new
						                                                                   Color[]{Color.WHITE, new Color(
						                                                                   0x00ffffff,
						                                                                   true)});

				final Graphics2D g2 = (Graphics2D) _overlay.getGraphics();

				final int rows = 3;
				final int cellSize = getHeight() / rows;
				final int cols = (getWidth()) / cellSize;

				for (int j = 0; j <= cols; j++)
				{
					for (int i = 0; i <= rows; i++)
					{
						if (i % 2 == j % 2)
						{
							g2.setColor(Color.GRAY);
						}
						else
						{
							g2.setColor(Color.WHITE);
						}
						g2.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
					}
				}

				g2.setComposite(_composite);

				g2.setPaint(horizontalGrad);
				g2.fillRect(0, 0, getWidth(), getHeight());

				g2.dispose();
			}
		});

		onChange(model);
	}

	private void onChange(final ColorModel colorModel)
	{
		_color = new Color((float) colorModel.getRed(), (float) colorModel.getGreen(),
		                   (float) colorModel.getBlue());
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

		g.setColor(Color.WHITE);
		g.fillRoundRect(4 + (int) ((width - 11) * _model.getAlpha()), 3, 3, height - 6, 2,
		                2);
		g.setColor(Color.BLACK);
		g.drawRoundRect(4 + (int) ((width - 11) * _model.getAlpha()), 3, 3, height - 6, 2,
		                2);

	}

}
