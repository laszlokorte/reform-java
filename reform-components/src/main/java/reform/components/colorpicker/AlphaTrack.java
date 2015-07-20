package reform.components.colorpicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class AlphaTrack extends JComponent {
    private BufferedImage _overlay;
    private final Color[] BASEH = {Color.WHITE, new Color
            (0x00FFFFFF, true)};
    private final Color[] BASEV = {Color.BLACK, new Color
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
            double alpha = 1.0 * e.getX() / getWidth();

            _model.setAlpha(Math.min(1, Math.max(0, alpha)));
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            super.mouseDragged(e);
            double alpha = 1.0 * e.getX() / getWidth();

            _model.setAlpha(Math.min(1, Math.max(0, alpha)));
        }
    };

    public AlphaTrack(ColorModel model) {
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


                Graphics2D g2 = (Graphics2D) _overlay.getGraphics();



                int rows = 3;
                int cellSize = getHeight() / rows;
                int cols = (getWidth()) / cellSize;

                for(int j=0;j<=cols;j++) {
                    for(int i=0;i<=rows;i++) {
                        if(i%2 == j%2) {
                            g2.setColor(Color.GRAY);
                        } else {
                            g2.setColor(Color.WHITE);
                        }
                        g2.fillRect(j*cellSize, i*cellSize, cellSize,cellSize);
                    }
                }

                g2.setComposite(_composite);

                g2.setPaint(horizontalGrad);
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

        g.setColor(Color.WHITE);
        g.fillRoundRect(4+(int) ((width - 11) * _model.getAlpha()), 3, 3,
                height - 6,
                2,2);
        g.setColor(Color.BLACK);
        g.drawRoundRect(4+(int) ((width - 11) * _model.getAlpha()), 3, 3,
                height - 6,
                2,2);

    }

}
