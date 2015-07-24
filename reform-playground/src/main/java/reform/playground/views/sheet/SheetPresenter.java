package reform.playground.views.sheet;

import reform.data.sheet.DataSet;
import reform.data.sheet.Solver;
import reform.evented.core.EventedSheet;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetPresenter
{
	private final TableModel _dataModel;
	private final JTable _table;
	private final Solver _solver;

	public SheetPresenter(EventedSheet sheet, DataSet dataSet, ExpressionParser parser) {
		_solver = new Solver(dataSet);
		_dataModel = new SheetTableModel(sheet, _solver, parser);
		_table = new SheetTable(_dataModel, dataSet);
		_table.setFocusable(false);
		_table.setSelectionBackground(Color.LIGHT_GRAY);
		_table.setSelectionForeground(Color.BLACK);
		_table.getColumnModel().getColumn(0).setWidth(100);
		_table.getColumnModel().getColumn(0).setPreferredWidth(100);
		_table.getColumnModel().getColumn(0).setMaxWidth(150);
		_table.setRowHeight(25);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		_table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableModelListener l = new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType() == TableModelEvent.DELETE) {
					if(_table.isEditing()) {
						_table.getCellEditor().cancelCellEditing();
					}
					int index = e.getFirstRow();
					if(index > 0) {
						_table.getSelectionModel().setSelectionInterval(index-1, index-1);
					} else if(index < _dataModel.getRowCount()) {
						_table.getSelectionModel().setSelectionInterval(index+1, index+1);
					} else {
						_table.getSelectionModel().clearSelection();
					}
				}
				if (e.getType() == TableModelEvent.INSERT) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							int viewRow = _table.convertRowIndexToView(e.getFirstRow());
							_table.scrollRectToVisible(_table.getCellRect(viewRow, 0, true));
							_table.editCellAt(e.getLastRow(), 1);
							_table.getSelectionModel().setSelectionInterval(e.getFirstRow(), e.getLastRow());
						}
					});
				}

				_table.repaint();
			}
		};
		_table.getModel().addTableModelListener(l);
	}

	public JComponent getComponent() {
		return _table;
	}

	public ListSelectionModel getSelectionModel() {
		return _table.getSelectionModel();
	}


}
