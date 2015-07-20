package reform.components.colorpicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;


public class ColorPickerPanel extends JPanel {
    private final ColorModel _model;
    private final HSVColorPlane _svPlane;
    private final HSVColorTrack _hueTrack;
    private final AlphaTrack _alphaTrack;
    private final JPanel _numberPanel = new JPanel();
    private final JPanel _hsvPanel = new JPanel();

    private static final int MAX = 100;

    private final SpinnerModel _colorSpinnerModelRed = new SpinnerNumberModel(1,
            0, MAX,
            1);
    private final SpinnerModel _colorSpinnerModelGreen = new
            SpinnerNumberModel(1,
            0, MAX,
            1);
    private final SpinnerModel _colorSpinnerModelBlue = new
            SpinnerNumberModel(1,
            0, MAX,
            1);
    private final SpinnerModel _colorSpinnerModelAlpha = new
            SpinnerNumberModel(1,
            0, MAX,
            1);

    private final JSpinner _fieldRed = new JSpinner(_colorSpinnerModelRed);
    private final JSpinner _fieldGreen = new JSpinner(_colorSpinnerModelGreen);
    private final JSpinner _fieldBlue = new JSpinner(_colorSpinnerModelBlue);
    private final JSpinner _fieldAlpha = new JSpinner(_colorSpinnerModelAlpha);

    public ColorPickerPanel(ColorModel model) {
        _model = model;
        _svPlane = new HSVColorPlane(_model);
        _hueTrack = new HSVColorTrack(_model);
        _alphaTrack = new AlphaTrack(_model);
        setLayout(new BorderLayout());
        add(_hsvPanel, BorderLayout.CENTER);
        add(_numberPanel, BorderLayout.SOUTH);


        _svPlane.setPreferredSize(new Dimension(250, 250));
        _hueTrack.setPreferredSize(new Dimension(24, 250));
        _alphaTrack.setPreferredSize(new Dimension(250, 24));

        _hsvPanel.setLayout(new BorderLayout());
        _hsvPanel.add(_svPlane, BorderLayout.CENTER);
        _hsvPanel.add(_hueTrack, BorderLayout.EAST);
        _hsvPanel.add(_alphaTrack,BorderLayout.SOUTH);


        _model.addListener(this::onChangePlane);

        configureSpinner(_fieldRed);
        configureSpinner(_fieldGreen);
        configureSpinner(_fieldBlue);
        configureSpinner(_fieldAlpha);


        _numberPanel.add(new JLabel("R:"));
        _numberPanel.add(_fieldRed);
        _numberPanel.add(new JLabel("G:"));
        _numberPanel.add(_fieldGreen);
        _numberPanel.add(new JLabel("B:"));
        _numberPanel.add(_fieldBlue);
        _numberPanel.add(new JLabel("A:"));
        _numberPanel.add(_fieldAlpha);

        _fieldRed.addChangeListener(this::onChangeField);
        _fieldGreen.addChangeListener(this::onChangeField);
        _fieldBlue.addChangeListener(this::onChangeField);
        _fieldAlpha.addChangeListener(this::onChangeField);

        onChangePlane(_model);
    }

    private void configureSpinner(final JSpinner fieldRed) {
    }

    private boolean _ownChange = false;

    private void onChangeField(final ChangeEvent changeEvent) {
    if(_ownChange) return;
        double r = 1.0 * (Integer)_fieldRed.getValue() / MAX;
        double g = 1.0 * (Integer)_fieldGreen.getValue() / MAX;
        double b = 1.0 * (Integer)_fieldBlue.getValue() / MAX;
        double a = 1.0 * (Integer)_fieldAlpha.getValue() / MAX;

        _model.setRGBA(r,g,b,a);
    }

    private void onChangePlane(final ColorModel colorModel) {
        _ownChange = true;
        _fieldRed.setValue((int)(_model.getRed() * MAX));
        _fieldGreen.setValue((int)(_model.getGreen() * MAX));
        _fieldBlue.setValue((int)(_model.getBlue() * MAX));
        _fieldAlpha.setValue((int)(_model.getAlpha() * MAX));
        _ownChange = false;
    }

    public ColorModel getModel() {
        return _model;
    }
}
