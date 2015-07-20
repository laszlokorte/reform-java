package reform.components.colorpicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class HueColorTrack extends JComponent
{
	private static final Color C1 = new Color(255, 0, 0);
	private static final Color C2 = new Color(255, 255, 0);
	private static final Color C3 = new Color(0, 255, 0);
	private static final Color C4 = new Color(0, 255, 255);
	private static final Color C5 = new Color(0, 0, 255);
	private static final Color C6 = new Color(255, 0, 255);

	private final float[] gradientPositions = {0f, 1f / 6, 2f / 6, 3f / 6, 4f / 6, 5f / 6, 1f};
	private final Color[] gradientColors = {C1, C2, C3, C4, C5, C6, C1};

	private final ColorModel _model;
	private LinearGradientPaint _linGrad;

	private final MouseAdapter _listener = new MouseAdapter()
	{


		@Override
		public void mousePressed(final MouseEvent e)
		{
			super.mousePressed(e);
			requestFocus();
			final double h = 1.0 * e.getY() / (getHeight() - 4);

			_model.setHSVA(Math.min(1, Math.max(0, h)), _model.getSaturation(), _model.getValue(), _model.getAlpha());
		}

		@Override
		public void mouseDragged(final MouseEvent e)
		{
			super.mousePressed(e);
			final double h = 1.0 * e.getY() / (getHeight() - 4);

			_model.setHSVA(Math.min(1, Math.max(0, h)), _model.getSaturation(), _model.getValue(), _model.getAlpha());
		}
	};

	public HueColorTrack(final ColorModel model)
	{
		_model = model;

		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(final ComponentEvent e)
			{
				_linGrad = new LinearGradientPaint(0, 0, 0, getHeight(), gradientPositions, gradientColors);
			}
		});

		addMouseListener(_listener);
		addMouseMotionListener(_listener);

		_model.addListener(this::onChange);
	}

	private void onChange(final ColorModel colorModel)
	{
		repaint();
	}

	@Override
	public void paintComponent(final Graphics og)
	{
		super.paintComponent(og);
		final Graphics2D g = (Graphics2D) og;

		final int width = getWidth();
		final int height = getHeight();

		g.setPaint(_linGrad);
		g.fillRect(0, 0, width, height);

		g.setColor(Color.WHITE);
		g.fillRoundRect(3, 4 + (int) ((height - 11) * _model.getHue()), width - 6, 3, 2, 2);
		g.setColor(Color.BLACK);
		g.drawRoundRect(3, 4 + (int) ((height - 11) * _model.getHue()), width - 6, 3, 2, 2);
	}
}
