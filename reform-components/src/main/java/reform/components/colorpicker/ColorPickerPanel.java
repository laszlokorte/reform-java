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

	public ColorPickerPanel(final ColorModel model, JTextField textfield)
	{
		_model = model;
		_svPlane = new SaturationValueColorPlane(_model);
		_hueTrack = new HueColorTrack(_model);
		_alphaTrack = new AlphaColorTrack(_model);
		_numberPanel.setLayout(new BoxLayout(_numberPanel, BoxLayout.LINE_AXIS));
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

		_numberPanel.add(textfield);

		final InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), "confirm");
	}

	private boolean _ownChange = false;

}
