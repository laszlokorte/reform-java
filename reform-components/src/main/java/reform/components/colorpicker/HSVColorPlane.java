package reform.components.colorpicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class HSVColorPlane extends JComponent {
    private BufferedImage _overlay;
    private final Color[] BASEH = {Color.BLACK, new Color
            (0x00000000, true)};
    private final float[] POS = {0, 1};
    private final Composite _composite = AlphaComposite.getInstance
            (AlphaComposite.DST_IN, 1.0F);

    private final ColorModel _model;

    private Color _color = Color.BLACK;

    private final MouseAdapter _listener = new MouseAdapter() {


        @Override
        public void mousePressed(final MouseEvent e) {
            super.mousePressed(e);
            requestFocus();
            double s = 1.0 * e.getX() / getWidth();
            double v = 1 - 1.0 * e.getY() / getHeight();

            _model.setHSVA(_model.getHue(), Math.min(1, Math.max(0, s)), Math
                    .min(1, Math.max(0, v)), _model.getAlpha());
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            super.mouseDragged(e);
            double s = 1.0 * e.getX() / getWidth();
            double v = 1 - 1.0 * e.getY() / getHeight();

            _model.setHSVA(_model.getHue(), Math.min(1, Math.max(0, s)), Math
                            .min(1, Math.max(0, v)),
                    _model.getAlpha());
        }
    };

    public HSVColorPlane(ColorModel model) {
        _model = model;
        setFocusable(true);
        addMouseListener(_listener);
        addMouseMotionListener(_listener);
        model.addListener(this::onChange);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                _overlay = new BufferedImage(getWidth(), getHeight(),
                        BufferedImage.TYPE_4BYTE_ABGR);

                LinearGradientPaint horizontalGrad = new LinearGradientPaint
                        (0, 0
                                , getWidth(), 0, POS, BASEH);


                LinearGradientPaint verticalGrad = new LinearGradientPaint
                        (0, getHeight(), 0,0, POS, BASEH);

                Graphics2D g2 = (Graphics2D) _overlay.getGraphics();

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                Composite oldComposit = g2.getComposite();

                g2.setComposite(_composite);
                g2.setPaint(horizontalGrad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setComposite(oldComposit);

                g2.setPaint(verticalGrad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        });

        onChange(model);
    }

    private void onChange(final ColorModel colorModel) {
        _color = Color.getHSBColor((float) colorModel.getHue(), 1, 1);
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        g2.setColor(_color);
        g2.fillRect(0, 0, width, height);

        g2.drawImage(_overlay, 0, 0, null);

        double cr = _model.getRed();
        double cg = _model.getGreen();
        double cb = _model.getBlue();

        if(cr*cr + cg*cg+cb*cb > 1.3) {
            g2.setColor(Color.BLACK);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawOval((int) (width * _model.getSaturation()) - 4, height - (int)
        (height *
                _model.getValue()
        ) - 4, 8, 8);

    }

}
