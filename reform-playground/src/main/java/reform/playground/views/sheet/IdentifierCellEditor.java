package reform.playground.views.sheet;

import reform.data.sheet.Definition;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IdentifierCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{

	private static final Color _selectionBackground = new Color(0x23AEEC);
	private static final Color _selectionForegroud = new Color(0xffffff);

	private final JLabel _label = new JLabel();
	private final JPanel _labelField = new JPanel();
	private final JTextField _textField = new SheetIdTextField(10);
	private final JPanel _panel = new JPanel();
	private String _currentValue = null;
	private String _initialValue = null;

	private final static Action NULL_ACTION = new AbstractAction()
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{

		}
	};

	IdentifierCellEditor()
	{
		_textField.setColumns(10);
		_textField.addActionListener(this);
		_textField.setVisible(false);
		_labelField.setLayout(new FlowLayout(FlowLayout.LEADING));
		_labelField.add(_label);
		_panel.add(_labelField);
		_panel.add(_textField);
		_panel.setOpaque(true);
		_panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
		_panel.setBackground(_selectionBackground);
		_labelField.setBackground(_selectionBackground);
		_label.setForeground(_selectionForegroud);
		_textField.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "ignore");
		_textField.getActionMap().put("ignore", NULL_ACTION);
		_panel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				super.mouseClicked(e);
				if (e.getClickCount() == 2)
				{
					_labelField.setVisible(false);
					_textField.setVisible(true);
					_textField.requestFocus();
				}
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
	                                             final int row, final int column)
	{
		_currentValue = ((Definition) value).getName();
		_initialValue = _currentValue;
		_textField.setText(_currentValue);
		_label.setText(_currentValue);

		_labelField.setVisible(true);
		_textField.setVisible(false);

		return _panel;
	}

	@Override
	public Object getCellEditorValue()
	{
		return _currentValue;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_currentValue = _textField.getText();
		fireEditingStopped();
	}

	@Override
	public boolean stopCellEditing()
	{
		if (!_initialValue.equals(_currentValue))
		{
			return super.stopCellEditing();
		}
		else
		{
			cancelCellEditing();
			return true;
		}
	}
}
