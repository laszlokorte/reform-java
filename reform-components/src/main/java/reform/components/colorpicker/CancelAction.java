package reform.components.colorpicker;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CancelAction extends AbstractAction
{

	private final ColorPicker _picker;

	CancelAction(final ColorPicker picker)
	{
		_picker = picker;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_picker.close();
	}
}
