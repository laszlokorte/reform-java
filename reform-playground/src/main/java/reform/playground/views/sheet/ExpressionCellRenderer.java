package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionCellRenderer extends JPanel implements TableCellRenderer
{
	private final JLabel _content = new JLabel();
	private final Color _selectionBackground = new Color(0x23AEEC);
	private final Color _selectionForegroud = new Color(0xffffff);

	ExpressionCellRenderer() {
		setLayout(new FlowLayout(FlowLayout.LEADING));
		setOpaque(true);
		add(_content);
	}


	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
	                                               final boolean hasFocus, final int row, final int column)
	{
		if (isSelected) {
			_content.setText("(5+7)*8");
			setBackground(_selectionBackground);
			_content.setForeground(_selectionForegroud);
		} else {
			_content.setText("196");
			setBackground(Color.WHITE);
			_content.setForeground(Color.BLACK);
		}

		return this;
	}
}
