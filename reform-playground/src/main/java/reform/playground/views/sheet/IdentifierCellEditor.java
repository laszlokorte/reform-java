package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class IdentifierCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{
	private final JTextField _textField = new JTextField("X");
	private String _currentValue = null;

	IdentifierCellEditor() {
		_textField.addActionListener(this);
	}

	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
	                                             final int row, final int column)
	{
		_currentValue = (String)value;
		_textField.setText(_currentValue);
		return _textField;
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
}
