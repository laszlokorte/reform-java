package reform.components.colorpicker;

import reform.rendering.icons.swing.ColorIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ColorPicker {

    private final ColorModel _model = new ColorModel();
    private final ColorIcon _icon = new ColorIcon(Color.BLACK, 18);
    private final JButton _button = new JButton(_icon);
    private static final JFrame _frame = new JFrame("Color Picker");
    private final ColorPickerPanel _panel = new ColorPickerPanel(_model);

    public ColorPicker() {
        _button.setBorder(null);
        _button.addActionListener(this::buttonClick);

        _frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        _frame.setResizable(false);

        _panel.getModel().addListener(this::onChange);
    }

    private void onChange(final ColorModel colorModel) {
        _icon.setColor(new Color((float)colorModel.getRed(), (float)colorModel.getGreen
                (),
                (float)colorModel.getBlue()));
        _button.repaint();
    }

    private void buttonClick(final ActionEvent actionEvent) {
        _frame.setContentPane(_panel);
        _frame.pack();
        _frame.setVisible(true);
        _frame.toFront();
    }

    public JButton getButton() {
        return _button;
    }
}
