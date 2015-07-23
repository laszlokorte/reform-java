package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetPresenter
{
	private final TableModel _dataModel = new SheetTableModel();
	private final JTable _table = new SheetTable(_dataModel);

	public SheetPresenter() {
		_table.setFocusable(false);
		_table.setSelectionBackground(Color.LIGHT_GRAY);
		_table.setSelectionForeground(Color.BLACK);
		_table.getColumnModel().getColumn(0).setWidth(100);
		_table.getColumnModel().getColumn(0).setPreferredWidth(100);
		_table.getColumnModel().getColumn(0).setMaxWidth(150);
		_table.setRowHeight(25);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	public JComponent getComponent() {
		return _table;
	}

}
