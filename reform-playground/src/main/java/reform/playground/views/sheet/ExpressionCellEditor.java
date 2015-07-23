package reform.playground.views.sheet;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.InvalidExpression;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{

	private final DataSet _dataSet;

	private final Color _selectionBackground = new Color(0x23AEEC);
	private final Color _selectionForegroud = new Color(0xffffff);

	private final JLabel _label = new JLabel();
	private final JPanel _labelField = new JPanel();
	private final JTextField _textField = new JTextField("X");
	private final JPanel _panel = new JPanel();
	private String _currentValue = null;

	ExpressionCellEditor(DataSet dataSet) {
		_dataSet = dataSet;
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

		ActionMap am = _panel.getActionMap();
		InputMap im = _panel.getInputMap();

		im.put(KeyStroke.getKeyStroke("alt UP"), "increment");
		im.put(KeyStroke.getKeyStroke("alt DOWN"), "decrement");

		am.put("increment", new AbstractAction()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				System.out.println(String.format("[OUT] %s", "increment"));
			}
		});

		am.put("decrement", new AbstractAction()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				System.out.println(String.format("[OUT] %s", "decrement"));
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
	                                             final int row, final int column)
	{
		Definition def = ((Definition)value);
		Expression expr = def.getExpression();
		_currentValue = expr.asString(false);
		_textField.setText(_currentValue);
		_label.setText(_currentValue);

		if(!_dataSet.hasValueFor(def.getId()) || expr instanceof InvalidExpression) {
			_label.setForeground(Color.RED.darker());
		} else {
			_label.setForeground(Color.WHITE);
		}

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

}
