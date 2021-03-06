package reform.playground.main;

import reform.math.Vec2i;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GlassLayerUI extends LayerUI<JComponent>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Vec2i _start = new Vec2i();
	private final Vec2i _current = new Vec2i();
	private final HashMap<RenderingHints.Key, Object> _renderOptions = new HashMap<>();
	private final Color _lineColor = new Color(0xff5b29);
	private final Stroke _stroke = new BasicStroke(4, BasicStroke.CAP_ROUND,
	                                               BasicStroke.JOIN_ROUND);
	private boolean _down = false;

	public GlassLayerUI()
	{
		_renderOptions.put(RenderingHints.KEY_ANTIALIASING,
		                   RenderingHints.VALUE_ANTIALIAS_ON);
		_renderOptions.put(RenderingHints.KEY_STROKE_CONTROL,
		                   RenderingHints.VALUE_STROKE_NORMALIZE);
	}

	@Override
	public void paint(final Graphics g, final JComponent c)
	{
		final Graphics2D g2 = (Graphics2D) g.create();

		// Paint the view.
		super.paint(g2, c);

		if (_down)
		{
			g2.setRenderingHints(_renderOptions);
			g2.setColor(_lineColor);
			g2.setStroke(_stroke);
			g2.drawLine(_start.x, _start.y, _current.x, _current.y);
			g2.fillOval(_start.x - 5, _start.y - 5, 10, 10);
			g2.fillOval(_current.x - 5, _current.y - 5, 10, 10);
		}

		g2.dispose();
	}

	@Override
	protected void processMouseEvent(final MouseEvent e, final JLayer<? extends
			JComponent> l)
	{
		final boolean newDown = e.getID() == MouseEvent.MOUSE_PRESSED && e
				.isControlDown();
		if (e.getID() == MouseEvent.MOUSE_EXITED || e.getID() == MouseEvent
				.MOUSE_ENTERED)
		{
			return;
		}
		if (_down != newDown)
		{
			_down = newDown;
			if (_down)
			{
				final Point p = SwingUtilities.convertPoint(e.getComponent(), e.getX(),
				                                            e.getY(), l);
				_current.set(p.x, p.y);
				_start.set(p.x, p.y);
				e.consume();
			}
		}
		l.repaint();
	}

	@Override
	protected void processMouseMotionEvent(final MouseEvent e, final JLayer<? extends
			JComponent> l)
	{
		final Point p = SwingUtilities.convertPoint(e.getComponent(), e.getX(), e.getY(),
		                                            l);
		if (_down)
		{
			_current.set(p.x, p.y);
			e.consume();
		}
		l.repaint();
	}

	@Override
	public void installUI(final JComponent c)
	{
		super.installUI(c);

		final JLayer<?> jLayer = (JLayer<?>) c;
		jLayer.setLayerEventMask(
				AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	@Override
	public void uninstallUI(final JComponent c)
	{
		final JLayer<?> jLayer = (JLayer<?>) c;
		jLayer.setLayerEventMask(0);
		super.uninstallUI(c);
	}

}
