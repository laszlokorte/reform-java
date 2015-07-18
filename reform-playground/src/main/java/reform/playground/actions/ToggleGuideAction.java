package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.evented.core.EventedProcedure;
import reform.stage.tooling.FormSelection;

public class ToggleGuideAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final EventedProcedure _eProcedure;
	private final Analyzer _analyzer;
	private final FormSelection _formSelection;

	public ToggleGuideAction(final EventedProcedure eProcedure,
			final Analyzer analyzer, final FormSelection formSelection) {
		_eProcedure = eProcedure;
		_analyzer = analyzer;
		_formSelection = formSelection;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (!_formSelection.isSet()) {
			return;
		}
		final Form form = _analyzer.getForm(_formSelection.getSelected());
		final DrawingType type = form.getType();

		if (type == DrawingType.Guide) {
			form.setType(DrawingType.Draw);
		} else {
			form.setType(DrawingType.Guide);
		}
		_eProcedure.publishFormChange(form);
	}

}
