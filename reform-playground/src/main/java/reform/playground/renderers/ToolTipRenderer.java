package reform.playground.renderers;

import reform.math.Vec2i;
import reform.stage.tooling.cursor.Cursor;

import java.awt.*;

public class ToolTipRenderer {
    private final Cursor _cursor;

    private final Color _background = new Color(0xbb222222, true);
    private final Color _text = new Color(0xffffff);
    private final Vec2i _offset = new Vec2i(15,-15);

    public ToolTipRenderer(Cursor cursor) {
        _cursor = cursor;
    }

    public void render(Graphics2D g2, double x, double y, String text) {
        x = (int)(x/6) * 6;
        y = (int)(y/6) * 6;
        g2.setColor(_background);
        g2.fillRoundRect((int) x + _offset.x, (int) y + _offset.y, 100, 30, 6, 6);
        g2.setColor(_text);
        g2.drawString(text, (int)x + _offset.x+ 10, (int)y + _offset.y + 20);
    }

    public void renderCentered(Graphics2D g2, double x, double y, String text) {
        x = (int)(x/6) * 6;
        y = (int)(y/6) * 6;
        g2.setColor(_background);
        g2.fillRoundRect((int) x + _offset.x - 50, (int) y + _offset.y, 100,
                30, 6, 6);
        g2.setColor(_text);
        g2.drawString(text, (int)x + _offset.x+ 10 - 50, (int)y + _offset.y + 20);
    }
}
