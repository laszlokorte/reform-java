package reform.components.expression;

import reform.data.sheet.Definition;
import reform.data.sheet.expression.Expression;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionEditor extends JTextField
{
	private final ArrayList<ChangeListener> _listeners = new ArrayList<>();
	private Expression _expression;

	public void setValue(final double value)
	{
		setText(String.format(Locale.ENGLISH, "%.2f",value));
	}

	public interface Parser {
		Expression parse(CharSequence charSeq);

		String getUniqueNameFor(String string, Definition definition);
	}

	private final Parser _parser;

	public ExpressionEditor(Parser parser) {
		_parser = parser;
		addActionListener(this::onAction);
	}

	public void setExpression(final Expression expression)
	{
		_expression = expression;
		super.setText(expression.asString(false));
	}

	public Expression getExpression()
	{
		if(_expression == null) {
			_expression = _parser.parse(getText());
		}
		return _expression;
	}

	public void setText(String text) {
		_expression = null;
		super.setText(text);
	}

	private void onAction(final ActionEvent actionEvent)
	{
		_expression = _parser.parse(getText());
		transferFocusUpCycle();
		for(int i=0;i<_listeners.size();i++) {
			_listeners.get(i).stateChanged(new ChangeEvent(this));
		}
	}

	public void addChangeListener(final ChangeListener listener)
	{
		_listeners.add(listener);
	}
}
