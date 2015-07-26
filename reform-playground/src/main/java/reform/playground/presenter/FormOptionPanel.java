package reform.playground.presenter;

import reform.components.colorpicker.ColorModel;
import reform.components.colorpicker.ColorPicker;
import reform.components.expression.ExpressionEditor;
import reform.core.analyzer.Analyzer;
import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.Form;
import reform.core.forms.PictureForm;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.project.Picture;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;
import reform.evented.core.EventedProcedure;
import reform.evented.core.EventedProject;
import reform.identity.Identifier;
import reform.rendering.icons.RulerIcon;
import reform.rendering.icons.swing.SwingIcon;
import reform.stage.tooling.FormSelection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class FormOptionPanel implements FormSelection.Listener, EventedProcedure
		.Listener, ActionListener
{

	private final ExpressionEditor.Parser _parser;
	private final JComponent _panel = Box.createHorizontalBox();
	private final Pool<ColorPanel> _colorPanels = new SimplePool<>(
			() -> new ColorPanel(this));
	private final Pool<ExpressionPanel> _expressionPanels = new SimplePool<>(
			() -> new ExpressionPanel(this));
	private final Pool<PictureIdPanel> _pictureIdPool = new SimplePool<>(
			() -> new PictureIdPanel(this));
	private final Pool<Component> _struts = new SimplePool<>(
			() -> Box.createHorizontalStrut(5));
	private final SwingIcon _rulerIcon = new SwingIcon(new RulerIcon());
	private final JToggleButton _guideToggle = new JToggleButton(_rulerIcon);
	private final FormSelection _selection;
	private final EventedProcedure _eProcedure;
	private final Analyzer _analyzer;
	private final Component _glue = Box.createGlue();
	private boolean _ownChange = false;
	private final EventedProject _eProject;

	public FormOptionPanel(final EventedProcedure eProcedure, final Analyzer analyzer, final FormSelection selection, final ExpressionEditor.Parser parser, final EventedProject eProject)
	{
		_eProcedure = eProcedure;
		_selection = selection;
		_analyzer = analyzer;
		_parser = parser;
		_eProject = eProject;

		_panel.add(_guideToggle);
		_guideToggle.setBorder(null);
		_guideToggle.setBackground(Color.LIGHT_GRAY);

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
	public void onInstructionAdded(final EventedProcedure procedure, final Instruction
			instruction, final InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure, final Instruction
			instruction, final InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure, final
	Instruction instruction, final InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure, final Instruction
			instruction, final InstructionGroup parent)
	{

	}

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
		releaseExpressionPanels();
		_struts.release();
		_panel.remove(_guideToggle);
		_panel.removeAll();

		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());
			final DrawingType drawType = form.getType();
			_panel.setVisible(true);

			_guideToggle.setSelected(drawType == DrawingType.Guide);
			_panel.add(_guideToggle);
			_panel.add(_struts.take());

			_panel.add(_glue);

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
				else if (type == Attribute.Type.Number || type == Attribute.Type.String)
				{
					final ExpressionPanel panel = getExpressionPanel(attr);
					_panel.add(panel.getComponent());
					panel.setEnabled(drawType == DrawingType.Draw);
				}else if (type == Attribute.Type.PictureId)
				{
					final PictureIdPanel panel = getPictureIdPanel(attr);
					_panel.add(panel.getComponent());
					panel.setEnabled(drawType == DrawingType.Draw);
				}
				_panel.add(_struts.take());

			}


		}
		else
		{
			_panel.setVisible(false);
		}

		_panel.revalidate();
		_panel.repaint();
		_colorPanels.clean(ColorPanel::dispose);
	}

	private ExpressionPanel getExpressionPanel(final Attribute attr)
	{
		final ExpressionPanel p = _expressionPanels.take();
		p.setAttribute(attr);

		return p;
	}


	private PictureIdPanel getPictureIdPanel(final Attribute attr)
	{
		final PictureIdPanel p = _pictureIdPool.take();
		p.setAttribute(attr);

		return p;
	}

	private ColorPanel getColorPanelFor(final Attribute attr)
	{
		final ColorPanel p = _colorPanels.take();
		p.setAttribute(attr);

		return p;
	}

	private void releaseColorPanels()
	{
		_colorPanels.release((p) -> {
			p.setAttribute(null);
			_panel.remove(p.getButton());
		});
	}

	private void releaseExpressionPanels()
	{
		_expressionPanels.release((p) -> {
			p.setAttribute(null);
			_panel.remove(p.getComponent());
		});
	}

	private void releasePictureIdPanels()
	{
		_pictureIdPool.release((p) -> {
			p.setAttribute(null);
			_panel.remove(p.getComponent());
		});
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());

			if (e.getSource() == _guideToggle)
			{
				form.setType(
						_guideToggle.isSelected() ? DrawingType.Guide : DrawingType
								.Draw);
				_colorPanels.eachActive((p) -> p.setEnabled(!_guideToggle.isSelected()));
				_expressionPanels.eachActive(
						(p) -> p.setEnabled(!_guideToggle.isSelected()));
				_pictureIdPool.eachActive(
						(p) -> p.setEnabled(!_guideToggle.isSelected()));
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

	private static class ExpressionPanel
	{
		private final ExpressionEditor _expressionEditor;
		private final FormOptionPanel _delegate;
		private Attribute _attribute;
		private final JLabel _label= new JLabel();
		private final JPanel _panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));

		ExpressionPanel(final FormOptionPanel delegate)
		{
			_delegate = delegate;
			_expressionEditor = new ExpressionEditor(_delegate._parser);
			_expressionEditor.addChangeListener(this::onModelChange);
			//_panel.add(_label);
			_panel.add(_expressionEditor);
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
				_expressionEditor.setToolTipText(attr.getName());
				//_label.setText(attr.getName());
			}
			_attribute = attr;
		}

		public Component getComponent()
		{
			return _panel;
		}

		public void setEnabled(final boolean enabled)
		{
			_panel.setVisible(enabled);
		}
	}

	private static class PictureIdPanel
	{
		private final JComboBox<Identifier<?extends Picture>> _optionPane = new JComboBox<>();
		private final FormOptionPanel _delegate;
		private Attribute _attribute;
		private final JPanel _panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));

		PictureIdPanel(final FormOptionPanel delegate)
		{
			_delegate = delegate;
			//_expressionEditor = new ExpressionEditor(_delegate._parser);
			//_expressionEditor.addChangeListener(this::onModelChange);
			//_panel.add(_label);
			_panel.add(_optionPane);
			_optionPane.addActionListener(this::onModelChange);
		}

		private void onModelChange(final ActionEvent changeEvent)
		{
			if (_attribute != null)
			{
				_attribute.setValue(new ConstantExpression(new Value(Identifier.getValue((Identifier)_optionPane.getSelectedItem()))));

				_delegate.onChange();
			}
		}

		void setAttribute(final Attribute attr)
		{
			if (attr != null)
			{
				_attribute = null; // prevent cycle

				//_expressionEditor.setExpression(attr.getValue());
				//_expressionEditor.setToolTipText(attr.getName());
				//_label.setText(attr.getName());
				_optionPane.removeAllItems();
				for(int i=0,j=_delegate._eProject.getPictureCount();i<j;i++) {
					_optionPane.addItem(_delegate._eProject.getPictures().get(i));
				}
				_optionPane.setSelectedItem(new Identifier(attr.getValue().getValueFor(null).getInteger()));
			}
			_attribute = attr;
		}

		public Component getComponent()
		{
			return _panel;
		}

		public void setEnabled(final boolean enabled)
		{
			_panel.setVisible(enabled);
		}
	}

	private static class ColorPanel
	{
		private final ExpressionEditor _expressionEditor;
		private final ColorPicker _colorPicker;
		private final FormOptionPanel _delegate;
		private Attribute _attribute;
		private boolean _expressionChanged = false;
		private boolean _colorChanged = false;
		ColorPanel(final FormOptionPanel delegate)
		{
			_expressionEditor = new ExpressionEditor(delegate._parser);
			_colorPicker = new ColorPicker(_expressionEditor);
			_colorPicker.getModel().addListener(this::onModelChange);
			_expressionEditor.addChangeListener(this::onExpressionChange);
			_delegate = delegate;
		}

		private void onExpressionChange(final ChangeEvent changeEvent)
		{
			if (_expressionChanged)
			{
				return;
			}
			_expressionChanged = true;
			final Expression expression = _expressionEditor.getExpression();

			if (expression instanceof ConstantExpression)
			{
				final ConstantExpression c = (ConstantExpression) expression;
				if (c.getValue().type == Value.Type.Color)
				{
					if (!_colorChanged)
					{
						_colorPicker.getModel().setHexARGB(c.getValue().getColor());
					}
				}
				else
				{
					_colorPicker.getModel().setHexARGB(0xff000000);
					_colorPicker.setMixed();
				}
			}
			else
			{
				_colorPicker.getModel().setHexARGB(0xff000000);
				_colorPicker.setMixed();
			}
			_attribute.setValue(expression);
			_delegate.onChange();
			_expressionChanged = false;
		}

		private void onModelChange(final ColorModel colorModel)
		{
			if (_expressionChanged)
			{
				return;
			}
			if (_attribute != null && _colorPicker.getButton().isEnabled())
			{
				_colorChanged = true;
				final Expression expression = new ConstantExpression(
						new Value(colorModel.getAlpha(), colorModel.getRed(),
						          colorModel.getGreen(), colorModel.getBlue()));

				_expressionEditor.setExpression(expression);
				onExpressionChange(null);
				_colorChanged = false;
			}
		}

		void setAttribute(final Attribute attr)
		{
			if (attr != null)
			{
				_attribute = null; // prevent cycle
				final Expression currentExpression = attr.getValue();

				_expressionEditor.setExpression(currentExpression);
				if (currentExpression instanceof ConstantExpression)
				{
					final ConstantExpression c = (ConstantExpression) currentExpression;

					_colorPicker.getModel().setHexARGB(c.getValue().getColor());
				}
				else
				{
					_colorPicker.getModel().setHexARGB(0);
					_colorPicker.setMixed();
				}

				_colorPicker.getButton().setToolTipText(attr.getName());


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
}
