package reform.components.colorpicker;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by laszlokorte on 20.07.15.
 */
public class CancelAction extends AbstractAction {

    private final ColorPicker _picker;

    CancelAction(ColorPicker picker) {
        _picker = picker;
    }
    @Override
    public void actionPerformed(final ActionEvent e) {
        _picker.close();
    }
}
