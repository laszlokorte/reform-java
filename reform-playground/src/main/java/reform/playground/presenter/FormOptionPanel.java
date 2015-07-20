package reform.playground.presenter;

import reform.components.colorpicker.ColorModel;
import reform.components.colorpicker.ColorPicker;
import reform.core.analyzer.Analyzer;
import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.Form;
import reform.core.graphics.Color;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.evented.core.EventedProcedure;
import reform.rendering.icons.RulerIcon;
import reform.rendering.icons.swing.SwingIcon;
import reform.stage.tooling.FormSelection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public final class FormOptionPanel implements FormSelection.Listener, EventedProcedure.Listener, ActionListener
{

	private static class ColorPanel
	{
		private Attribute<reform.core.graphics.Color> _attribute;
		private final ColorPicker _colorPicker = new ColorPicker();
		private final FormOptionPanel _delegate;

		ColorPanel(final FormOptionPanel delegate)
		{
			_colorPicker.getModel().addListener(this::onModelChange);
			_delegate = delegate;
		}

		private void onModelChange(final ColorModel colorModel)
		{
			if (_attribute != null && _colorPicker.getButton().isEnabled())
			{
				final Color c = _attribute.getValue();
				c.setRed(colorModel.getRed());
				c.setGreen(colorModel.getGreen());
				c.setBlue(colorModel.getBlue());
				c.setAlpha(colorModel.getAlpha());

				_delegate.onChange();
			}
		}

		void setAttribute(final Attribute<Color> attr)
		{
			if (attr != null)
			{
				_attribute = null; // prevent cycle
				final Color currentColor = attr.getValue();

				_colorPicker.getModel().setRGBA(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(),
				                                currentColor.getAlpha());
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

	private final JPanel _panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 1));
	private final JLabel _label = new JLabel("");

	private final Pool<ColorPanel> _colorPanels = new SimplePool<>(() -> new ColorPanel(this));
	private final ArrayList<ColorPanel> _currentColorPanels = new ArrayList<>();

	private final SwingIcon _rulerIcon = new SwingIcon(new RulerIcon());
	private final JToggleButton _guideToggle = new JToggleButton(_rulerIcon);

	private final FormSelection _selection;
	private final EventedProcedure _eProcedure;
	private final Analyzer _analyzer;

	public FormOptionPanel(final EventedProcedure eProcedure, final Analyzer analyzer, final FormSelection selection)
	{
		_eProcedure = eProcedure;
		_selection = selection;
		_analyzer = analyzer;
		_panel.add(_label);

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
		if(_ownChange) {
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

		if (_selection.isSet())
		{
			final Form form = _analyzer.getForm(_selection.getSelected());
			final DrawingType drawType = form.getType();
			_label.setText(form.getName().getValue());
			_panel.setVisible(true);

			final AttributeSet attributes = form.getAttributes();
			for (int i = 0, j = attributes.size(); i < j; i++)
			{
				final Attribute<?> attr = attributes.get(i);
				final Class<?> type = attr.getType();

				if (type == Color.class)
				{
					@SuppressWarnings("unchecked") final ColorPanel panel = getColorPanelFor((Attribute<Color>) attr);
					_panel.add(panel.getButton());
					panel.setEnabled(drawType == DrawingType.Draw);

				}
			}

			_guideToggle.setSelected(drawType == DrawingType.Guide);
		}
		else
		{
			_panel.setVisible(false);
		}

		_panel.revalidate();
		_panel.repaint();
		_colorPanels.clean(ColorPanel::dispose);
	}

	private ColorPanel getColorPanelFor(final Attribute<Color> attr)
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
