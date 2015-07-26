package reform.components.expression;

import reform.data.sheet.Definition;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.InvalidExpression;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;

public class ExpressionEditor extends JPanel
{
	private static final Color _errorColor = Color.RED.darker();
	private static final Border _errorBorder = BorderFactory.createLineBorder(_errorColor);
	private static final Border _defaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

	private final ArrayList<ChangeListener> _listeners = new ArrayList<>();
	private final Parser _parser;
	private Expression _expression;

	private String _prevValue = "";
	private final JTextField _textField = new JTextField(_prevValue);
	private final JPanel _labelPanel = new JPanel();
	private final JLabel _label = new JLabel(_prevValue);

	public ExpressionEditor(final Parser parser)
	{
		_parser = parser;

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		_labelPanel.add(_label);
		_labelPanel.setLayout(new BoxLayout(_labelPanel, BoxLayout.LINE_AXIS));
		add(Box.createHorizontalStrut(3));
		_label.setVerticalAlignment(JLabel.CENTER);
		_label.setHorizontalAlignment(JLabel.LEADING);

		setMinimumSize(new Dimension(40, 30));
		setPreferredSize(new Dimension(90, 30));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		_textField.setBorder(null);
		_textField.setBackground(null);
		add(_labelPanel);
		_label.setForeground(Color.GRAY);
		_labelPanel.setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		setBorder(_defaultBorder);

		add(_textField);
		_textField.setVisible(false);
		_textField.addActionListener(this::onAction);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				super.mouseClicked(e);

				_textField.setVisible(true);
				_labelPanel.setVisible(false);
				_textField.requestFocus();

			}
		});

		_textField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				super.focusGained(e);
			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				onAction(null);
				super.focusLost(e);
			}
		});
	}

	public Expression getExpression()
	{
		if (_expression == null)
		{
			_expression = _parser.parse(_textField.getText());
		}
		return _expression;
	}

	public void setExpression(final Expression expression)
	{
		if (expression instanceof InvalidExpression)
		{
			_label.setForeground(_errorColor);
			setBorder(_errorBorder);
		}
		else
		{
			setBorder(_defaultBorder);
			_label.setForeground(Color.GRAY);
		}
		_expression = expression;
		String string = expression.asString(false);
		_prevValue = string;
		_textField.setText(string);
		_label.setText(string);
	}

	public void setValue(final double value)
	{
		String string = String.format(Locale.ENGLISH, "%.2f", value);
		setText(string);
		_label.setForeground(Color.GRAY);
		setBorder(_defaultBorder);

	}

	public void setText(final String text)
	{
		_label.setForeground(Color.GRAY);
		setBorder(_defaultBorder);

		_expression = null;
		_prevValue = text;
		_label.setText(text);
		_textField.setText(text);
	}

	private void onAction(final ActionEvent actionEvent)
	{
		setExpression(_parser.parse(_textField.getText()));

		transferFocusUpCycle();

		_textField.setVisible(false);
		_labelPanel.setVisible(true);
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
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean
			pressed)
	{
		super.processKeyBinding(ks, e, condition, pressed);

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			cancel();
		}

		return true;
	}

	private void cancel()
	{
		transferFocusUpCycle();
		_textField.setText(_prevValue);
		_textField.setVisible(false);
		_labelPanel.setVisible(true);
	}

	public interface Parser
	{
		Expression parse(CharSequence charSeq);

		String getUniqueNameFor(String string, Definition definition);
	}
}
