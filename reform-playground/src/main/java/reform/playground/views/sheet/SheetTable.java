package reform.playground.views.sheet;

import reform.data.sheet.DataSet;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class SheetTable extends JTable
{
	private final ExpressionCellRenderer _expressionCellRenderer;
	private final ExpressionCellEditor _expressionCellEditor;
	private final IdentifierCellRenderer _identifierCellRenderer = new IdentifierCellRenderer();
	private final IdentifierCellEditor _identifierCellEditor = new IdentifierCellEditor();

	public SheetTable(final TableModel dataModel, final DataSet dataSet)
	{
		super(dataModel);
		_expressionCellEditor = new ExpressionCellEditor(dataSet);
		_expressionCellRenderer = new ExpressionCellRenderer(dataSet);
	}

	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column)
	{
		if (column == 0)
		{
			return _identifierCellRenderer;
		}
		else if (column == 1)
		{
			return _expressionCellRenderer;
		}

		return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(final int row, final int column)
	{
		if (column == 0)
		{
			return _identifierCellEditor;
		}
		else if (column == 1)
		{
			return _expressionCellEditor;
		}
		return super.getCellEditor(row, column);
	}


}
