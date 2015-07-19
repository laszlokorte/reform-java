package reform.playground.presenter;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.LineForm;
import reform.core.forms.relations.ConstantAngle;
import reform.core.forms.relations.ConstantScaleFactor;
import reform.core.graphics.DrawingType;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.blocks.ForLoopInstruction;
import reform.core.procedure.instructions.blocks.IfConditionInstruction;
import reform.core.procedure.instructions.single.RotateInstruction;
import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.core.runtime.relations.RotationAngle;
import reform.core.runtime.relations.ScaleFactor;
import reform.evented.core.EventedProcedure;
import reform.rendering.icons.swing.ColorIcon;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;

public final class FormOptionPanel implements FormSelection.Listener,
		 EventedProcedure.Listener, ChangeListener {
	private final JPanel _panel = new JPanel(
			new FlowLayout(FlowLayout.RIGHT));
    private final JLabel _label = new JLabel("");
    private final JLabel _guideLabel = new JLabel("Guide:");

    private final ColorIcon _colorIconBackground = new ColorIcon(new Color
            (0xB7B7B7), 18);
    private final ColorIcon _colorIconStroke = new ColorIcon(Color.BLACK, 18);
    private final JButton _colorChooserBackground = new JButton(_colorIconBackground);
    private final JButton _colorChooserStroke = new JButton(_colorIconStroke);

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

        _panel.add(_colorChooserBackground);
        _colorChooserBackground.setBorder(null);
        _panel.add(_colorChooserStroke);
        _colorChooserStroke.setBorder(null);


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
        if(_selection.isSet()) {
            Form form = _analyzer.getForm(_selection.getSelected());
            Class<? extends Form> formClass = form.getClass();
            DrawingType drawType = form.getType();
            _label.setText(form
                    .getName().getValue());
            _panel.setVisible(true);

            _colorChooserStroke.setEnabled(drawType == DrawingType.Draw);
            _colorChooserBackground.setEnabled(drawType == DrawingType.Draw);
            _colorChooserBackground.setVisible(
                    formClass != LineForm
                    .class);
            _guideCheckbox.setSelected(drawType == DrawingType.Guide);
        } else {
            _panel.setVisible(false);
        }
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        if(_selection.isSet()) {
            Form form = _analyzer.getForm(_selection.getSelected());
            form.setType(_guideCheckbox.isSelected() ? DrawingType.Guide :
                    DrawingType.Draw);

            _eProcedure.publishFormChange(form);
        }
    }
}
