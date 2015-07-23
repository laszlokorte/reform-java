package reform.playground.views.sheet;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Objects;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetTableModel extends AbstractTableModel implements TableModel
{
	private static  final String[] COLUMNS = {"Key", "Value"};
	private static  final String[] ROWS = new String[10];

	@Override
	public int getRowCount()
	{
		return ROWS.length;
	}

	@Override
	public int getColumnCount()
	{
		return COLUMNS.length;
	}

	@Override
	public String getColumnName(final int columnIndex)
	{
		return COLUMNS[columnIndex];
	}

	@Override
	public Class<String> getColumnClass(final int columnIndex)
	{
		return String.class;
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex)
	{
		return true;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		return ROWS[rowIndex];
	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
	{
		ROWS[rowIndex] = (String)aValue;
	}
}
