package reform.components.expression;

import reform.data.sheet.Definition;
import reform.data.sheet.expression.Expression;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Locale;

public class ExpressionEditor extends JTextField
{
	private final ArrayList<ChangeListener> _listeners = new ArrayList<>();
	private Expression _expression;

	public interface Parser
	{
		Expression parse(CharSequence charSeq);

		String getUniqueNameFor(String string, Definition definition);
	}

	private final Parser _parser;

	public ExpressionEditor(final Parser parser)
	{
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
		if (_expression == null)
		{
			_expression = _parser.parse(getText());
		}
		return _expression;
	}

	public void setValue(final double value)
	{
		setText(String.format(Locale.ENGLISH, "%.2f", value));
	}


	public void setText(final String text)
	{
		_expression = null;
		super.setText(text);
	}

	private void onAction(final ActionEvent actionEvent)
	{
		_expression = _parser.parse(getText());
		transferFocusUpCycle();
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).stateChanged(new ChangeEvent(this));
		}
	}

	public void addChangeListener(final ChangeListener listener)
	{
		_listeners.add(listener);
	}

	@Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed)
	{
		super.processKeyBinding(ks, e, condition, pressed);

		return true;
	}
}
