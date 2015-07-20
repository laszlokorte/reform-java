package reform.playground.presenter;

import reform.components.colorpicker.ColorModel;
import reform.components.colorpicker.ColorPicker;
import reform.core.analyzer.Analyzer;
import reform.core.attributes.Attribute;
import reform.core.attributes.AttributeSet;
import reform.core.forms.Form;
import reform.core.forms.LineForm;
import reform.core.graphics.*;
import reform.core.graphics.Color;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.FormSelection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public final class FormOptionPanel implements FormSelection.Listener,
		 EventedProcedure.Listener, ChangeListener {
    private static class ColorPanel {
        private Attribute<reform.core.graphics.Color> _attribute;
        private final ColorPicker _colorPicker = new ColorPicker();
        private final JButton _colorPickerButton = _colorPicker
                .getButton();
        private final ChangeListener _listener;

        ColorPanel(ChangeListener listener) {
            _colorPicker.getModel().addListener(this::onModelChange);
            _listener = listener;
        }

        private void onModelChange(final ColorModel colorModel) {
            if(_attribute != null) {
                Color c = _attribute.getValue();
                c.setRed(colorModel.getRed());
                c.setGreen(colorModel.getGreen());
                c.setBlue(colorModel.getBlue());
                c.setAlpha(colorModel.getAlpha());
            }
        }

        void setAttribute(Attribute<Color> attr) {
            if(attr != null) {
                _attribute = null; // prevent cycle
                Color currentColor = attr.getValue();

                _colorPicker.getModel().setRGBA(currentColor.getRed(),
                        currentColor.getGreen(), currentColor.getBlue(),
                        currentColor.getAlpha());
            }
            _attribute = attr;
        }

        public Component getButton() {
            return _colorPicker
                    .getButton();
        }

        public void dispose() {
            _colorPicker.dispose();
        }
    }

	private final JPanel _panel = new JPanel(
			new FlowLayout(FlowLayout.RIGHT));
    private final JLabel _label = new JLabel("");
    private final JLabel _guideLabel = new JLabel("Guide:");

    private final Pool<ColorPanel> _colorPanels = new SimplePool<>(()-> {
        return new ColorPanel(this);
    });



    private final JCheckBox _guideCheckbox = new JCheckBox();

	private final FormSelection _selection;
	private final EventedProcedure _eProcedure;
    private final Analyzer _analyzer;

    public FormOptionPanel(final EventedProcedure eProcedure,
                           Analyzer analyzer,
                           final FormSelection selection) {
		_eProcedure = eProcedure;
		_selection = selection;
        _analyzer = analyzer;
		_panel.add(_label);

        _panel.add(_guideLabel);
        _panel.add(_guideCheckbox);
        _guideCheckbox.setBorder(null);

        _guideCheckbox.setFocusable(false);
        _guideCheckbox.addChangeListener(this);

		selection.addListener(this);
		onSelectionChanged(selection);

		eProcedure.addListener(this);
	}

	public Component getComponent() {
		return _panel;
	}


	@Override
	public void onInstructionAdded(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

    @Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure,
			final Instruction instruction, final InstructionGroup parent) {

	}

	@Override
	public void onFormChanged(final EventedProcedure procedure,
			final Form form) {
        if (_selection.isSet() && _selection.isSelected(form.getId())) {
            onSelectionChanged(_selection);
        }
	}

    @Override
    public void onSelectionChanged(final FormSelection selection) {
        releaseColorPanels();

        if (_selection.isSet()) {
            Form form = _analyzer.getForm(_selection.getSelected());
            Class<? extends Form> formClass = form.getClass();
            DrawingType drawType = form.getType();
            _label.setText(form
                    .getName().getValue());
            _panel.setVisible(true);

            AttributeSet attributes = form.getAttributes();
            for (int i = 0, j = attributes.size(); i < j; i++) {
                Attribute<?> attr = attributes.get(i);
                Class<?> type = attr.getType();

                if (type == Color.class) {
                    if (drawType == DrawingType.Draw) {
                        ColorPanel panel = getColorPanelFor((Attribute<Color>)
                                attr);
                        _panel.add(panel.getButton());
                    }
                }
            }

            _guideCheckbox.setSelected(drawType == DrawingType.Guide);
        } else {
            _panel.setVisible(false);
        }

        _panel.revalidate();
        _panel.repaint();
        _colorPanels.clean(ColorPanel::dispose);
    }

    private ColorPanel getColorPanelFor(final Attribute<Color> attr) {
        ColorPanel p = _colorPanels.take();
        p.setAttribute(attr);

        return p;
    }

    private void releaseColorPanels() {
        _colorPanels.release((ColorPanel p) -> {
            p.setAttribute(null);
            _panel.remove(p.getButton());
        });
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        if(_selection.isSet()) {
            Form form = _analyzer.getForm(_selection.getSelected());

            if(e.getSource() == _guideCheckbox) {
                form.setType(_guideCheckbox.isSelected() ? DrawingType.Guide :
                        DrawingType.Draw);
            }

            _eProcedure.publishFormChange(form);
        }
    }
}
