package reform.playground.actions;

import reform.evented.core.EventedSheet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

public class RemoveDefinitionAction extends AbstractAction implements ListSelectionListener
{
	private final ListSelectionModel _sheetSelection;
	private final EventedSheet _eSheet;

	public RemoveDefinitionAction(final ListSelectionModel sheetSelection, final EventedSheet eSheet)
	{
		_sheetSelection = sheetSelection;
		_eSheet = eSheet;

		_sheetSelection.addListSelectionListener(this);
		setEnabled(_sheetSelection.isSelectionEmpty());
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		if (!_sheetSelection.isSelectionEmpty())
		{
			_eSheet.removeDefinition(_sheetSelection.getMinSelectionIndex());
		}
	}

	@Override
	public void valueChanged(final ListSelectionEvent e)
	{
		setEnabled(!_sheetSelection.isSelectionEmpty());
	}
}
