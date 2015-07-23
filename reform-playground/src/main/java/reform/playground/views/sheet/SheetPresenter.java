package reform.playground.views.sheet;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.expression.Expression;
import reform.data.syntax.Lexer;
import reform.data.syntax.Parser;
import reform.data.syntax.SimpleDelegate;
import reform.data.syntax.Token;
import reform.evented.core.EventedSheet;
import reform.identity.Identifier;
import sun.jvm.hotspot.ui.treetable.TreeTableModelAdapter;

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
	private final DataSet _dataSet = new DataSet();
	private final Solver _solver = new Solver(_dataSet);
	private final Lexer _lexer = getLexer();
	private final Parser.ParserDelegate _delegate;
	private final Parser<Expression> _parser;

	public SheetPresenter(EventedSheet sheet) {
		_delegate = new SimpleDelegate(sheet.getRaw());
		_parser = new Parser(_delegate);
		_dataModel = new SheetTableModel(sheet, _solver, _lexer, _parser);
		_table = new SheetTable(_dataModel, _dataSet);
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


	private static Lexer getLexer()
	{
		Lexer.Generator generator = new Lexer.Generator();

		generator.ignore("\\s+");
		generator.ignore("\\u00A0+");

		generator.add(Token.Type.ParenthesisLeft, "\\(");
		generator.add(Token.Type.ParenthesisRight, "\\)");


		generator.add(Token.Type.Operator, "\\-");
		generator.add(Token.Type.Operator, "\\+");
		generator.add(Token.Type.Operator, "\\/");
		generator.add(Token.Type.Operator, "\\*");
		generator.add(Token.Type.Operator, "\\%");
		generator.add(Token.Type.Operator, "\\^");

		generator.add(Token.Type.ArgumentSeparator, ",");
		generator.add(Token.Type.LiteralValue, "(0|([1-9][0-9]*))(\\.[0-9]*)?");
		generator.add(Token.Type.LiteralValue, "\"[^\"]+\"");
		generator.add(Token.Type.Identifier, "\\$[a-zA-Z_][_a-zA-Z0-9]*");
		generator.add(Token.Type.FunctionName, "[a-z]+");
		generator.add(Token.Type.LiteralValue, "\"[^\"]*\"");

		return generator.getLexer();
	}

}
