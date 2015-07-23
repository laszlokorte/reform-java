package reform.playground.views.sheet;

import reform.identity.Identifier;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetTable extends JTable
{
	private final ExpressionCellRenderer _expressionCellRenderer = new ExpressionCellRenderer();
	private final ExpressionCellEditor _expressionCellEditor = new ExpressionCellEditor();
	private final IdentifierCellRenderer _identifierCellRenderer = new IdentifierCellRenderer();
	private final IdentifierCellEditor _identifierCellEditor = new IdentifierCellEditor();

	public SheetTable(final TableModel dataModel)
	{
		super(dataModel);
	}

	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column)
	{
		if(column == 0)
		{
			return _identifierCellRenderer;
		} else if(column == 1) {
			return _expressionCellRenderer;
		}

		return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(final int row, final int column)
	{
		if(column == 0)
		{
			return _identifierCellEditor;
		} else if(column == 1) {
			return _expressionCellEditor;
		}
		return super.getCellEditor(row, column);
	}


}
