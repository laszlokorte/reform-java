package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class IdentifierCellRenderer extends JPanel implements TableCellRenderer
{
	private final JLabel _content = new JLabel();
	private final Color _selectionBackground = new Color(0x23AEEC);
	private final Color _selectionForegroud = new Color(0xffffff);

	IdentifierCellRenderer() {
		setLayout(new FlowLayout(FlowLayout.LEADING));
		setOpaque(true);
		add(_content);
	}


	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
	                                               final boolean hasFocus, final int row, final int column)
	{
		_content.setText("X");

		if (isSelected) {
			setBackground(_selectionBackground);
			_content.setForeground(_selectionForegroud);
		} else {
			setBackground(Color.WHITE);
			_content.setForeground(Color.BLACK);
		}

		return this;
	}
}
