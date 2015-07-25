package reform.playground.views.sheet;

import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.evented.core.EventedSheet;
import reform.identity.Identifier;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SheetTableModel extends AbstractTableModel implements TableModel,
		EventedSheet.Listener
{
	private static final String[] COLUMNS = {"Key", "Value"};
	private static final Class<?>[] COLUMN_TYPES = {Definition.class, Definition.class};
	private final EventedSheet _eSheet;
	private final Solver _solver;
	private final ExpressionParser _parser;

	SheetTableModel(final EventedSheet eSheet, final Solver solver, final
	ExpressionParser parser)
	{
		_eSheet = eSheet;
		_solver = solver;
		_parser = parser;

		eSheet.addListener(this);
	}

	@Override
	public int getRowCount()
	{
		return _eSheet.size();
	}

	@Override
	public int getColumnCount()
	{
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		return _eSheet.getDefinition(rowIndex);
	}

	@Override
	public String getColumnName(final int columnIndex)
	{
		return COLUMNS[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex)
	{
		return COLUMN_TYPES[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex)
	{
		return true;
	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int
			columnIndex)
	{
		final String string = (String) aValue;
		switch (columnIndex)
		{
			case 0:
				_eSheet.setName(rowIndex, _parser.getUniqueNameFor(string,
				                                                   _eSheet.getDefinition(
						                                                   rowIndex)));
				break;
			case 1:
				_eSheet.setExpression(rowIndex, _parser.parse(string));
				break;
			default:
				throw new RuntimeException("invalid column");
		}
	}

	@Override
	public void onNameChanged(final EventedSheet picture, final Identifier<? extends
			Definition> dataDefinition, final int index)
	{
		// needed to update reference labels
		// TODO: extract label updating into separat method.
		_solver.evaluate(_eSheet.getRaw());
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onDefinitionChanged(final EventedSheet picture, final Identifier<?
			extends Definition> dataDefinition, final int index)
	{
		_solver.evaluate(_eSheet.getRaw());
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onDefinitionAdded(final EventedSheet eventedSheet, final Definition
			definition, final int index)
	{
		_solver.evaluate(_eSheet.getRaw());

		fireTableRowsInserted(index, index);
	}

	@Override
	public void onDefinitionRemoved(final EventedSheet eventedSheet, final Definition definition, final int index)
	{
		fireTableRowsDeleted(index, index);
	}
}
