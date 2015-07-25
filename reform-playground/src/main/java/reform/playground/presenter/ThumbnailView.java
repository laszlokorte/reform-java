package reform.playground.presenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ThumbnailView extends JComponent
{
	public interface Adapter
	{

		int getCount();

		int getWidth(int i);

		int getHeight(int i);

		boolean isSelected(int i);

		void draw(Graphics2D g2, int i);

		void onClickAtIndex(int clicked);

		void onDoubleClick();

		void onDoubleClickAtIndex(int clicked);

	}

	private static final long serialVersionUID = 1L;

	private final HashMap<RenderingHints.Key, Object> _renderOptions = new HashMap<>();

	private final List<Integer> _rowOffsets = new ArrayList<>();

	private final Adapter _adapter;

	private final Color _selectionColor = new Color(0x23A9E5);

	public ThumbnailView(final Adapter adapter)
	{
		_adapter = adapter;

		_renderOptions.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		_renderOptions.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		setPreferredSize(new Dimension(100, 100));
		setOpaque(false);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	@Override
	protected void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		_rowOffsets.clear();
		final Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHints(_renderOptions);

		final int width = getWidth();
		final int height = getHeight();
		final int maxWidth = 200;
		final int maxHeight = getHeight() - 20;

		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, width, height);
		int x = 10;

		g2.setColor(Color.WHITE);

		for (int i = 0; i < _adapter.getCount(); i++)
		{
			_rowOffsets.add(x);

			final int picWidth = _adapter.getWidth(i);
			final int picHeight = _adapter.getHeight(i);
			final double scale = Math.min(1.0 * maxWidth / picWidth, 1.0 * maxHeight / picHeight);
			final int w = (int) Math.round(scale * picWidth);
			final int h = (int) Math.round(scale * picHeight);

			if (_adapter.isSelected(i))
			{
				g2.setColor(_selectionColor);
				g2.fillRect(x - 5, (height - h) / 2 - 5, w + 10, h + 10);
			}

			g2.setColor(Color.WHITE);
			g2.fillRect(x, (height - h) / 2, w, h);

			final AffineTransform oldTrans = g2.getTransform();
			final Shape oldClip = g2.getClip();

			g2.clipRect(x, (height - h) / 2, w, h);
			g2.translate(x, (height - h) / 2);
			g2.scale(scale, scale);

			_adapter.draw(g2, i);

			g2.setTransform(oldTrans);
			g2.setClip(oldClip);

			x += 10 + w;
		}

		g2.setColor(Color.DARK_GRAY.brighter());
		g2.fillRect(x, (height - 100) / 2, 100, 100);
		g2.setColor(Color.lightGray);
		g2.fillRect(x + 47, (height - 40) / 2, 6, 40);
		g2.fillRect(x + 30, (height - 6) / 2, 40, 6);

		_rowOffsets.add(x);
		g2.dispose();
	}

	public void update()
	{

		doLayout();
		revalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize()
	{
		final int height = Math.max(getHeight(), 120);
		final int maxWidth = 200;
		final int maxHeight = height - 20;
		int totalWidth = 10;

		for (int i = 0; i < _adapter.getCount(); i++)
		{
			final int picWidth = _adapter.getWidth(i);
			final int picHeight = _adapter.getHeight(i);
			final double scale = Math.min(1.0 * maxWidth / picWidth, 1.0 * maxHeight / picHeight);
			final int w = (int) Math.round(scale * picWidth);

			totalWidth += 10 + w;
		}

		return new Dimension(totalWidth + 130, 120);
	}

	@Override
	protected void processMouseEvent(final MouseEvent e)
	{
		super.processMouseEvent(e);

		if (e.getID() == MouseEvent.MOUSE_PRESSED)
		{
			final int clicked = indexAt(e.getX());

			if (e.isAltDown())
			{
				if (clicked > -1)
				{
					_adapter.onDoubleClickAtIndex(clicked);
				}
			}
			else
			{
				if (clicked > -1)
				{
					final int height = Math.max(getHeight(), 120);
					final int maxWidth = 200;
					final int maxHeight = height - 20;

					final int picWidth = _adapter.getWidth(clicked);
					final int picHeight = _adapter.getHeight(clicked);
					final double scale = Math.min(1.0 * maxWidth / picWidth, 1.0 * maxHeight / picHeight);
					final int width = (int) Math.round(scale * picWidth);

					scrollRectToVisible(new Rectangle(_rowOffsets.get(clicked) - 10, 0, width + 20, getHeight()));
					_adapter.onClickAtIndex(clicked);
				}
				else if (clicked > -2)
				{
					_adapter.onDoubleClick();

					doLayout();
					scrollRectToVisible(new Rectangle(getWidth() + 100, 0, 120, getHeight()));
				}
			}
			repaint();
		}
	}

	private int indexAt(final int x)
	{
		int result = -2;
		if (_rowOffsets.isEmpty() && x <= 110)
		{
			return -1;
		}
		else if (!_rowOffsets.isEmpty())
		{
			final int last = _rowOffsets.get(_rowOffsets.size() - 1);
			if (x > last && x < last + 100)
			{
				return -1;
			}
			else if (x > last + 100)
			{
				return -2;
			}
			for (int i = 0, j = _rowOffsets.size() - 1; i < j; i++)
			{
				if (x > _rowOffsets.get(i))
				{
					result = i;
				}
				else
				{
					break;
				}
			}
		}

		return result;
	}
}
