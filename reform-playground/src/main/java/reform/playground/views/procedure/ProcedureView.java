package reform.playground.views.procedure;

import reform.core.pool.Pool;
import reform.core.pool.PoolFactory;
import reform.core.pool.SimplePool;
import reform.core.runtime.errors.RuntimeError;
import reform.playground.presenter.StepSnapshotCollector;
import reform.rendering.paint.StripePaint;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public final class ProcedureView extends JComponent implements InstructionFocus
		.Listener, StepSnapshotCollector.Listener
{
	private static final long serialVersionUID = 1L;
	private static final Color _selectionColor = new Color(0x23A9E5);
	private static final Border _errorBorder = BorderFactory.createLineBorder(
			Color.red.darker(), 2);
	private final static BaseItem[] _emptyViews = new BaseItem[0];
	private final Box _inner = Box.createVerticalBox();
	private final Adapter _adapter;
	private final Pool<NullItem> _nullItemPool = new SimplePool<>(
			new PoolFactory<NullItem>()
			{
				@Override
				public NullItem create()
				{
					return new NullItem(ProcedureView.this._adapter);
				}
			});
	private final Pool<GroupItem> _groupItemPool = new SimplePool<>(
			new PoolFactory<GroupItem>()
			{
				@Override
				public GroupItem create()
				{
					return new GroupItem(ProcedureView.this._adapter);
				}
			});
	private final Pool<PicturedItem> _picturedItemPool = new SimplePool<>(
			new PoolFactory<PicturedItem>()
			{
				@Override
				public PicturedItem create()
				{
					return new PicturedItem(ProcedureView.this._adapter);
				}
			});
	private final Paint _inactivePaint = StripePaint.getPaint();
	private BaseItem[] _views = _emptyViews;
	private BaseItem _focused = null;
	private int _focusedIndex = -1;
	private boolean _incomplete = true;
	private BufferedImage _emptyImg;
	public ProcedureView(final Adapter adapter)
	{
		setLayout(new BorderLayout());
		this.add(_inner, BorderLayout.NORTH);
		this.add(Box.createVerticalBox(), BorderLayout.SOUTH);

		_adapter = adapter;

		setMinimumSize(new Dimension(300, 300));
		requireUpdate();
		updateListIfNeeded();
		setDoubleBuffered(true);

	}

	@Override
	public void onCollectionCompleted(final StepSnapshotCollector collector)
	{
		updateListIfNeeded();
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus)
	{
		// TODO Auto-generated method stub

	}

	private void updateListIfNeeded()
	{
		if (_incomplete)
		{
			SwingUtilities.invokeLater(this::update);
		}
	}

	private void update()
	{
		_emptyImg = null;
		if (_views.length != _adapter.getSize())
		{
			_views = new BaseItem[_adapter.getSize()];
		}
		_inner.removeAll();
		_nullItemPool.release();
		_groupItemPool.release();
		_picturedItemPool.release();

		boolean shouldFocus = false;
		for (int i = 0, j = _adapter.getSize(); i < j; i++)
		{
			if (_adapter.isEmptySlot(i))
			{
				final NullItem item = _nullItemPool.take();
				item.setIndex(i);
				item.setFocused(false);
				_views[i] = item;
			}
			else if (_adapter.isGroup(i))
			{
				final GroupItem item = _groupItemPool.take();
				item.setFocused(false);
				item.setIndex(i);
				item.setIndent(_adapter.getIndentation(i));
				item.setText(_adapter.getDescription(i));
				_views[i] = item;
			}
			else
			{
				Image img = _adapter.getImageAt(i);
				if (img == null)
				{
					img = getEmptyImage(_adapter.getImageWidth(),
					                    _adapter.getImageHeight());
				}
				final PicturedItem item = _picturedItemPool.take();
				item.setFocused(false);
				item.setIndex(i);
				item.setIndent(_adapter.getIndentation(i));
				item.setText(_adapter.getDescription(i));
				item.setImage(img);
				_views[i] = item;

			}

			if (_adapter.hasFailed(i))
			{
				_views[i].setError(_adapter.getError(i).getMessage());
			}
			else
			{
				_views[i].resetError();
			}

			if (i == _focusedIndex)
			{
				if (_focused == null)
				{
					shouldFocus = true;
				}
				_views[i].setFocused(true);
				_focused = _views[i];

			}
			else
			{
				_views[i].setFocused(false);
			}
			_inner.add(_views[i]);

		}

		revalidate();
		repaint();


		if (shouldFocus)
		{
			SwingUtilities.invokeLater(this::scrollToFocus);
		}

		_incomplete = false;
	}

	private void scrollToFocus()
	{
		final Rectangle bounds = _focused.getBounds();
		final Rectangle rect = new Rectangle(bounds.x, bounds.y - 15, bounds.width,
		                                     bounds.height + 30);
		scrollRectToVisible(rect);
	}

	private Image getEmptyImage(int width, int height)
	{
		if (width == 0 || height == 0)
		{
			width = 100;
			height = 100;
		}
		height = Math.max(40, height);
		if (_emptyImg == null)
		{
			_emptyImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			final Graphics2D g = (Graphics2D) _emptyImg.getGraphics();
			g.setPaint(_inactivePaint);
			g.fillRect(0, 0, width, height);
		}
		return _emptyImg;
	}

	public void requireUpdate()
	{
		_incomplete = true;
	}

	public int getFocus()
	{
		return _focusedIndex;
	}

	public void setFocus(final int i)
	{
		if (_focused != null && !_incomplete)
		{
			_focused.setFocused(false);
		}
		_focusedIndex = i;

		if (i > -1 && _views.length > i && !_incomplete)
		{
			_focused = _views[i];
			_views[i].setFocused(true);

			final Rectangle bounds = _views[i].getBounds();
			final Rectangle rect = new Rectangle(bounds.x, bounds.y - 15, bounds.width,
			                                     bounds.height + 30);

			scrollRectToVisible(rect);
		}
		else
		{
			_focused = null;
		}
	}

	private static JTextArea getTextArea()
	{
		final JTextArea textArea = new JTextArea();
		textArea.setFocusable(false);
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		textArea.setForeground(Color.BLACK);
		textArea.setDisabledTextColor(Color.BLACK);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		textArea.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				e.getComponent().getParent().dispatchEvent(e);
			}
		});
		final DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		return textArea;

	}

	public interface Adapter
	{

		int getSize();

		void onSelect(int clicked);

		BufferedImage getImageAt(int index);

		int getImageWidth();

		int getImageHeight();

		boolean hasFailed(int index);

		boolean hasBeenEvaluated(int index);

		RuntimeError getError(int index);

		String getDescription(int index);

		void removeInstruction(int clicked);

		int getIndentation(int i);

		boolean isEmptySlot(int index);

		boolean isGroup(int i);

	}

	private static abstract class BaseItem extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private final Adapter _adapter;
		private int _index = -1;

		BaseItem(final Adapter adapter)
		{
			_adapter = adapter;

			setLayout(new BorderLayout(5, 5));
			setBackground(Color.WHITE);
			enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		}

		public void setIndex(final int index)
		{
			_index = index;
		}

		@Override
		protected void processMouseEvent(final MouseEvent e)
		{
			super.processMouseEvent(e);

			if (e.getID() == MouseEvent.MOUSE_PRESSED)
			{
				_adapter.onSelect(_index);
			}
		}

		public void setFocused(final boolean f)
		{
			if (f)
			{
				setBackground(_selectionColor);
			}
			else
			{
				setBackground(Color.WHITE);
			}
		}

		public void setError(final String error)
		{

		}

		public void resetError()
		{

		}

	}

	private static class NullItem extends BaseItem
	{
		private static final long serialVersionUID = 1L;

		public NullItem(final Adapter adapter)
		{
			super(adapter);
			setBackground(Color.WHITE);

			setPreferredSize(new Dimension(300, 10));
		}
	}

	private static class GroupItem extends BaseItem
	{
		private static final long serialVersionUID = 1L;

		private final JTextArea _label;

		public GroupItem(final Adapter adapter)
		{
			super(adapter);
			_label = getTextArea();

			_label.setBackground(Color.white);
			_label.setForeground(Color.black);
			_label.setDisabledTextColor(Color.black);

			add(_label, BorderLayout.CENTER);
			setBackground(Color.white);

			setPreferredSize(new Dimension(300, 30));
		}

		public void setIndent(final int indent)
		{
			setBorder(BorderFactory.createEmptyBorder(7, 20 * indent + 5, 0, 20));
		}

		public void setText(final String text)
		{
			_label.setText(text);
		}

		@Override
		public void setFocused(final boolean f)
		{
			super.setFocused(f);
			if (f)
			{
				_label.setBackground(_selectionColor);
				_label.setForeground(Color.WHITE);
				_label.setDisabledTextColor(Color.WHITE);
			}
			else
			{
				_label.setBackground(Color.WHITE);
				_label.setForeground(Color.BLACK);
				_label.setDisabledTextColor(Color.BLACK);
			}
		}
	}

	private static class PicturedItem extends BaseItem
	{
		private static final long serialVersionUID = 1L;

		private static final Color _errorColor = Color.red.darker();
		private final static Font _errorFont = new JLabel().getFont().deriveFont(
				Font.BOLD);
		private final JTextArea _label;
		private final JLabel _errorLabel = new JLabel();
		private final JLabel _iconLabel;
		private final ImageIcon _icon;
		private final Dimension _size = new Dimension(300, 30);
		private final Dimension _iconSize = new Dimension(30, 30);
		private int _indent = -1;

		public PicturedItem(final Adapter adapter)
		{
			super(adapter);

			_label = getTextArea();
			_label.setBackground(Color.white);
			_label.setForeground(Color.black);
			_label.setDisabledTextColor(Color.black);

			_icon = new ImageIcon();
			_iconLabel = new JLabel(_icon);
			add(_iconLabel, BorderLayout.WEST);

			_errorLabel.setForeground(_errorColor);

			add(_label, BorderLayout.CENTER);

			setMinimumSize(_size);
			setPreferredSize(_size);
		}

		public void setImage(final Image image)
		{

			final int height;
			if (image != null)
			{
				_icon.setImage(image);
				_iconSize.setSize(image.getWidth(null), image.getHeight(null));
				_iconLabel.setMinimumSize(_iconSize);
				height = Math.max(image.getHeight(null) + 10, 70);
			}
			else
			{
				_icon.setImage(null);
				height = 70;
			}

			_size.setSize(300, height);
		}

		public void setIndent(final int indent)
		{
			if (_indent != indent)
			{
				setBorder(BorderFactory.createEmptyBorder(5, 20 * indent + 5, 5, 20));
				_indent = indent;
			}
		}

		public void setText(final String text)
		{
			_label.setText(text);
		}

		@Override
		public void setFocused(final boolean f)
		{
			super.setFocused(f);
			if (f)
			{
				_label.setBackground(_selectionColor);
				_label.setForeground(Color.WHITE);
				_label.setDisabledTextColor(Color.WHITE);
			}
			else
			{
				_label.setBackground(Color.WHITE);
				_label.setForeground(Color.BLACK);
				_label.setDisabledTextColor(Color.BLACK);
			}
		}

		@Override
		public void setError(final String error)
		{
			super.setError(error);
			_errorLabel.setFont(_errorFont);
			_errorLabel.setText(error);
			_iconLabel.setBorder(_errorBorder);
			_iconLabel.setBackground(Color.red);
			add(_errorLabel, BorderLayout.SOUTH);
		}

		@Override
		public void resetError()
		{
			super.resetError();
			_errorLabel.setFont(_errorFont);
			_iconLabel.setBorder(null);
			_iconLabel.setBackground(null);
			remove(_errorLabel);
		}
	}
}
