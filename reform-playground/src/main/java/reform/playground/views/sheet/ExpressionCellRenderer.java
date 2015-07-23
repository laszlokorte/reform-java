package reform.playground.views.sheet;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.InvalidExpression;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.xml.crypto.Data;
import java.awt.*;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionCellRenderer extends JPanel implements TableCellRenderer
{
	private final JLabel _content = new JLabel();
	private final Color _selectionBackground = new Color(0x23AEEC);
	private final Color _selectionForegroud = new Color(0xffffff);
	private final DataSet _dataSet;

	ExpressionCellRenderer(DataSet dataSet) {
		_dataSet = dataSet;
		setLayout(new FlowLayout(FlowLayout.LEADING));
		setOpaque(true);
		add(_content);
	}


	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
	                                               final boolean hasFocus, final int row, final int column)
	{
		Definition definition = (Definition) value;
		if (isSelected) {
			_content.setText(definition.getExpression().asString(false));
			setBackground(_selectionBackground);
			if(!_dataSet.hasValueFor(definition.getId()) || definition.getExpression() instanceof InvalidExpression) {
				_content.setForeground(Color.RED.darker());
			} else {
				_content.setForeground(_selectionForegroud);
			}
		} else {
			setBackground(Color.WHITE);
			if(!_dataSet.hasValueFor(definition.getId()) || definition.getExpression() instanceof InvalidExpression) {
				_content.setText(definition.getExpression().asString(false));
				_content.setForeground(Color.RED.darker());
			} else {
				_content.setText(_dataSet.lookUp(definition.getId()).asString());
				_content.setForeground(Color.BLACK);
			}
		}

		return this;
	}
}
