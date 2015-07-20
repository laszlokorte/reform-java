package reform.components.colorpicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;


public class ColorPickerPanel extends JPanel
{
	private final ColorModel _model;
	private final SaturationValueColorPlane _svPlane;
	private final HueColorTrack _hueTrack;
	private final AlphaColorTrack _alphaTrack;
	private final JPanel _numberPanel = new JPanel();
	private final JPanel _hsvPanel = new JPanel();

	private static final int MAX = 100;

	private final SpinnerModel _colorSpinnerModelRed = new SpinnerNumberModel(1, 0, MAX, 1);
	private final SpinnerModel _colorSpinnerModelGreen = new SpinnerNumberModel(1, 0, MAX, 1);
	private final SpinnerModel _colorSpinnerModelBlue = new SpinnerNumberModel(1, 0, MAX, 1);
	private final SpinnerModel _colorSpinnerModelAlpha = new SpinnerNumberModel(1, 0, MAX, 1);

	private final JSpinner _fieldRed = new JSpinner(_colorSpinnerModelRed);
	private final JSpinner _fieldGreen = new JSpinner(_colorSpinnerModelGreen);
	private final JSpinner _fieldBlue = new JSpinner(_colorSpinnerModelBlue);
	private final JSpinner _fieldAlpha = new JSpinner(_colorSpinnerModelAlpha);

	public ColorPickerPanel(final ColorModel model)
	{
		_model = model;
		_svPlane = new SaturationValueColorPlane(_model);
		_hueTrack = new HueColorTrack(_model);
		_alphaTrack = new AlphaColorTrack(_model);
		setLayout(new BorderLayout());
		add(_hsvPanel, BorderLayout.CENTER);
		add(_numberPanel, BorderLayout.SOUTH);


		_svPlane.setPreferredSize(new Dimension(250, 250));
		_hueTrack.setPreferredSize(new Dimension(24, 250));
		_alphaTrack.setPreferredSize(new Dimension(250, 24));

		_hsvPanel.setLayout(new BorderLayout());
		_hsvPanel.add(_svPlane, BorderLayout.CENTER);
		_hsvPanel.add(_hueTrack, BorderLayout.EAST);
		_hsvPanel.add(_alphaTrack, BorderLayout.SOUTH);


		_model.addListener(this::onChangePlane);

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

		final InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), "confirm");
	}

	private boolean _ownChange = false;

	private void onChangeField(final ChangeEvent changeEvent)
	{
		if (_ownChange)
		{
			return;
		}

		final double r = 1.0 * (Integer) _fieldRed.getValue() / MAX;
		final double g = 1.0 * (Integer) _fieldGreen.getValue() / MAX;
		final double b = 1.0 * (Integer) _fieldBlue.getValue() / MAX;
		final double a = 1.0 * (Integer) _fieldAlpha.getValue() / MAX;

		_model.setRGBA(r, g, b, a);
	}

	private void onChangePlane(final ColorModel colorModel)
	{
		_ownChange = true;
		_fieldRed.setValue((int) Math.round(_model.getRed() * MAX));
		_fieldGreen.setValue((int) Math.round(_model.getGreen() * MAX));
		_fieldBlue.setValue((int) Math.round(_model.getBlue() * MAX));
		_fieldAlpha.setValue((int) Math.round(_model.getAlpha() * MAX));
		_ownChange = false;
	}

}
