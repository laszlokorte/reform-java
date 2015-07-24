package reform.playground.presenter;

import reform.components.colorpicker.ColorModel;
import reform.components.colorpicker.ColorPicker;
import reform.components.expression.ExpressionEditor;
import reform.core.analyzer.Analyzer;
import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;
import reform.evented.core.EventedProcedure;
import reform.rendering.icons.RulerIcon;
import reform.rendering.icons.swing.SwingIcon;
import reform.stage.tooling.FormSelection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public final class FormOptionPanel implements FormSelection.Listener, EventedProcedure.Listener, ActionListener
{

	private final ExpressionEditor.Parser _parser;

	private static class NumberPanel
	{
		private Attribute _attribute;
		private final ExpressionEditor _expressionEditor;

		private final FormOptionPanel _delegate;

		NumberPanel(final FormOptionPanel delegate)
		{
			_delegate = delegate;
			_expressionEditor = new ExpressionEditor(_delegate._parser);
			_expressionEditor.addChangeListener(this::onModelChange);
		}

		private void onModelChange(final ChangeEvent changeEvent)
		{
			if (_attribute != null)
			{
				_attribute.setValue(_expressionEditor.getExpression());

				_delegate.onChange();
			}
		}

		void setAttribute(final Attribute attr)
		{
			if (attr != null)
			{
				_attribute = null; // prevent cycle

				_expressionEditor.setExpression(attr.getValue());
			}
			_attribute = attr;
		}

		public Component getComponent()
		{
			return _expressionEditor;
		}

		public void setEnabled(final boolean enabled)
		{
			_expressionEditor.setVisible(enabled);
		}
	}

	private static class ColorPanel
	{
		private Attribute _attribute;
		private final ExpressionEditor _expressionEditor;
		private final ColorPicker _colorPicker;
		private final FormOptionPanel _delegate;

		ColorPanel(final FormOptionPanel delegate)
		{
			_expressionEditor = new ExpressionEditor(delegate._parser);
			_colorPicker = new ColorPicker(_expressionEditor);
			_colorPicker.getModel().addListener(this::onModelChange);
			_expressionEditor.addChangeListener(this::onExpressionChange);
			_delegate = delegate;
		}

		private boolean _ownChange = false;
		private void onExpressionChange(final ChangeEvent changeEvent)
		{
			if(_ownChange) return;
			_ownChange = true;
			Expression expression = _expressionEditor.getExpression();

			if(expression instanceof ConstantExpression) {
				ConstantExpression c = (ConstantExpression) expression;
				if(c.getValue().type == Value.Type.Color)
				{
					_colorPicker.getModel().setHexARGB(c.getValue().getColor());
				} else {
					_colorPicker.getModel().setHexARGB(0xff000000);
					_colorPicker.setMixed();
				}
			} else {
				_colorPicker.getModel().setHexARGB(0xff000000);
				_colorPicker.setMixed();
			}
			_attribute.setValue(expression);
			_delegate.onChange();
			_ownChange = false;
		}

		private void onModelChange(final ColorModel colorModel)
		{
			if(_ownChange) return;
			if (_attribute != null && _colorPicker.getButton().isEnabled())
			{
				Expression expression = new ConstantExpression(
						new Value(colorModel.getAlpha(), colorModel.getRed(), colorModel.getGreen(),
						          colorModel.getBlue()));

				_expressionEditor.setExpression(expression);
				onExpressionChange(null);
			}
		}

		void setAttribute(final Attribute attr)
		{
			if (attr != null)
			{
				_attribute = null; // prevent cycle
				final Expression currentExpression = attr.getValue();

				_expressionEditor.setExpression(currentExpression);
				if(currentExpression instanceof ConstantExpression) {
					ConstantExpression c = (ConstantExpression) currentExpression;

					_colorPicker.getModel().setHexARGB(c.getValue().getColor());
				} else {
					_colorPicker.getModel().setHexARGB(0);
					_colorPicker.setMixed();
				}

			}
			_attribute = attr;
		}

		public Component getButton()
		{
			return _colorPicker.getButton();
		}

		public void dispose()
		{
			_colorPicker.dispose();
		}

		public void setEnabled(final boolean enabled)
		{
			_colorPicker.getButton().setEnabled(enabled);
			if (!enabled)
			{
				_colorPicker.dispose();
			}
		}
	}

	private final JPanel _panel = new JPanel();

	private final Pool<ColorPanel> _colorPanels = new SimplePool<>(() -> new ColorPanel(this));
	private final ArrayList<ColorPanel> _currentColorPanels = new ArrayList<>();

	private final Pool<NumberPanel> _numberPanels = new SimplePool<>(() -> new NumberPanel(this));
	private final ArrayList<NumberPanel> _currentNumberPanels = new ArrayList<>();

	private final SwingIcon _rulerIcon = new SwingIcon(new RulerIcon());
	private final JToggleButton _guideToggle = new JToggleButton(_rulerIcon);

	private final FormSelection _selection;
	private final EventedProcedure _eProcedure;
	private final Analyzer _analyzer;

	public FormOptionPanel(final EventedProcedure eProcedure, final Analyzer analyzer, final FormSelection selection, ExpressionEditor.Parser parser)
	{
		_eProcedure = eProcedure;
		_selection = selection;
		_analyzer = analyzer;
		_parser = parser;

		_panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));

		_panel.add(_guideToggle);
		_guideToggle.setBorder(null);

		_guideToggle.setFocusable(false);
		_guideToggle.addActionListener(this);

		selection.addListener(this);
		onSelectionChanged(selection);

		eProcedure.addListener(this);
	}

	public Component getComponent()
	{
		return _panel;
	}


	@Override
	public void onInstructionAdded(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{

	}

	private boolean _ownChange = false;

	@Override
	public void onFormChanged(final EventedProcedure procedure, final Form form)
	{
		if (_ownChange)
		{
			return;
		}
		if (_selection.isSet() && _selection.isSelected(form.getId()))
		{
			onSelectionChanged(_selection);
		}
	}

	@Override
	public void onSelectionChanged(final FormSelection selection)
	{
		releaseColorPanels();
		releaseNumberPanels();
		_panel.remove(_guideToggle);
		_panel.removeAll();

		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());
			final DrawingType drawType = form.getType();
			_panel.setVisible(true);

			final AttributeSet attributes = form.getAttributes();
			for (int i = 0, j = attributes.size(); i < j; i++)
			{
				final Attribute attr = attributes.get(i);
				final Attribute.Type type = attr.getType();

				if (type == Attribute.Type.Color)
				{
					final ColorPanel panel = getColorPanelFor(attr);
					_panel.add(panel.getButton());
					panel.setEnabled(drawType == DrawingType.Draw);

				}
				else if (type == Attribute.Type.Number)
				{
					NumberPanel panel = getNumberPanelFor(attr);
					_panel.add(panel.getComponent());
					panel.setEnabled(drawType == DrawingType.Draw);
				}
				_panel.add(Box.createHorizontalStrut(5));

			}

			_panel.add(Box.createHorizontalGlue());

			_guideToggle.setSelected(drawType == DrawingType.Guide);
			_panel.add(_guideToggle);
		}
		else
		{
			_panel.setVisible(false);
		}

		_panel.revalidate();
		_panel.repaint();
		_colorPanels.clean(ColorPanel::dispose);
	}

	private NumberPanel getNumberPanelFor(final Attribute attr)
	{
		final NumberPanel p = _numberPanels.take();
		p.setAttribute(attr);
		_currentNumberPanels.add(p);

		return p;
	}

	private ColorPanel getColorPanelFor(final Attribute attr)
	{
		final ColorPanel p = _colorPanels.take();
		p.setAttribute(attr);
		_currentColorPanels.add(p);

		return p;
	}

	private void releaseColorPanels()
	{
		_colorPanels.release((ColorPanel p) -> {
			p.setAttribute(null);
			_panel.remove(p.getButton());
		});

		_currentColorPanels.clear();
	}

	private void releaseNumberPanels()
	{
		_numberPanels.release((NumberPanel p) -> {
			p.setAttribute(null);
			_panel.remove(p.getComponent());
		});

		_currentNumberPanels.clear();
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());

			if (e.getSource() == _guideToggle)
			{
				form.setType(_guideToggle.isSelected() ? DrawingType.Guide : DrawingType.Draw);
				_currentColorPanels.forEach((p) -> p.setEnabled(!_guideToggle.isSelected()));
				_currentNumberPanels.forEach((p) -> p.setEnabled(!_guideToggle.isSelected()));
			}

			_ownChange = true;
			_eProcedure.publishFormChange(form);
			_ownChange = false;
		}
	}

	private void onChange()
	{
		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());

			_ownChange = true;
			_eProcedure.publishFormChange(form);
			_ownChange = false;
		}
	}
}
