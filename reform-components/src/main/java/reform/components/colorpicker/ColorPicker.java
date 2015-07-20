package reform.components.colorpicker;

import reform.rendering.icons.swing.ColorIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ColorPicker
{

	private final ColorModel _model = new ColorModel();
	private final ColorIcon _icon = new ColorIcon(Color.BLACK);
	private final JButton _button = new JButton(_icon);
	private static final JFrame _frame = new JFrame("Color Picker");
	private static ColorPicker _current = null;
	private final ColorPickerPanel _panel = new ColorPickerPanel(_model);
	private int _ARGB;
	private final ActionMap _actionMap = new ActionMap();

	public ColorPicker()
	{
		_button.setBorder(null);
		_button.addActionListener(this::buttonClick);

		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		_frame.setResizable(false);

		_model.addListener(this::onChange);

		_actionMap.put("cancel", new CancelAction(this));
	}

	private void onChange(final ColorModel colorModel)
	{
		_icon.setColor(
				new Color((float) colorModel.getRed(), (float) colorModel.getGreen(), (float) colorModel.getBlue(),
				          (float) colorModel.getAlpha()));
		_button.repaint();
	}

	private void buttonClick(final ActionEvent actionEvent)
	{
		_panel.setActionMap(_actionMap);
		_frame.setContentPane(_panel);
		_frame.pack();
		_frame.setVisible(true);
		_frame.toFront();

		_current = this;
	}

	public JButton getButton()
	{
		return _button;
	}

	public ColorModel getModel()
	{
		return _model;
	}

	public void dispose()
	{
		if (_current == this)
		{
			close();
		}
	}

	void close()
	{
		_frame.setVisible(false);
		_current = null;
	}
}
