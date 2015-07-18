package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.tooling.ToolState;

import java.awt.*;

public class ToolStateDescriptionRenderer implements CanvasRenderer {

	private final Stage _stage;
	private final ToolState _toolState;

	private final Asset _cropDot = new Dot(6, 2, new Color(0xB14BEC),
			new Color(0xB14BEC));

	private boolean _preview;
    private final Color _backgroundColor = new Color(0x88333333, true);

    public ToolStateDescriptionRenderer(final Stage stage, final ToolState toolState) {
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		final Vec2i size = _stage.getSize();

        if(! _preview && !_toolState.isPreviewMode()) {
            g2.setColor(_backgroundColor);
            g2.fillRect(10,10, 500, 30);
        }
	}

	public void setPreview(final boolean b) {
		_preview = b;
	}
}
