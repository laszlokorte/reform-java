package reform.playground.views.sheet;

import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.InvalidExpression;
import reform.data.syntax.Lexer;
import reform.data.syntax.Parser;
import reform.evented.core.EventedSheet;
import reform.identity.Identifier;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetTableModel extends AbstractTableModel implements TableModel, EventedSheet.Listener
{
	private final EventedSheet _eSheet;
	private final Solver _solver;
	private final Lexer _lexer;
	private final Parser<Expression> _parser;
	private static  final String[] COLUMNS = {"Key", "Value"};
	private static  final Class<?>[] COLUMN_TYPES = {Definition.class, Definition.class};

	SheetTableModel(EventedSheet eSheet, Solver solver, Lexer lexer, Parser parser) {
		_eSheet = eSheet;
		_solver = solver;
		_lexer = lexer;
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
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		return _eSheet.getDefinition(rowIndex);
	}

	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
	{
		String string =  (String) aValue;
		switch (columnIndex) {
			case 0:
				_eSheet.setName(rowIndex, _eSheet.getUniqueNameFor(string, _eSheet.getDefinition(rowIndex)));
				break;
			case 1:
				Expression expr;
				try {
					expr = _parser.parse(_lexer.tokenize(string));
				} catch(Lexer.LexingException e) {
					expr = new InvalidExpression(string);
				} catch(Parser.ParsingException e) {
					expr = new InvalidExpression(string);
				}
				_eSheet.setExpression(rowIndex, expr);
				break;
			default: throw new RuntimeException("invalid column");
		}
	}

	@Override
	public void onNameChanged(final EventedSheet picture, final Identifier<? extends Definition> dataDefinition, int index)
	{
		// needed to update reference labels
		// TODO: extract label updating into separat method.
		_solver.evaluate(_eSheet.getRaw());
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onDefinitionChanged(final EventedSheet picture, final Identifier<? extends Definition> dataDefinition, int index)
	{
		_solver.evaluate(_eSheet.getRaw());
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onDefinitionAdded(final EventedSheet eventedSheet, final Definition definition, final int index)
	{
		_solver.evaluate(_eSheet.getRaw());

		fireTableRowsInserted(index, index);
	}

	@Override
	public void onDefinitionRemoved(final EventedSheet eventedSheet, final Definition definition, final int index)
	{
		System.out.println(String.format("[OUT] %s", index));
		fireTableRowsDeleted(index, index);
	}
}
