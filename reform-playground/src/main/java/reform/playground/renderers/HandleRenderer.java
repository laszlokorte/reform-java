package reform.playground.renderers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.Handle;
import reform.stage.tooling.ToolState;

public class HandleRenderer implements CanvasRenderer {

	private final Stage _stage;
	private final ToolState _toolState;

	private final Asset _cropDot = new Dot(6, 2, new Color(0x78C98B),
			new Color(0x78C98B));

	private final Asset _cropDotActive = new Dot(7, 1, new Color(0x78C98B),
			new Color(0x78C98B));
	private boolean _preview;

	public HandleRenderer(final Stage stage, final ToolState toolState) {
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		final Vec2i size = _stage.getSize();

		g2.translate((width - size.x) / 2, (height - size.y) / 2);

		Handle active = null;
		if (_preview) {
			g2.setComposite(
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		}

		final List<Handle> handles = _toolState.getHandles();
		for (int i = 0, j = handles.size(); i < j; i++) {
			final Handle p = handles.get(i);
			if (_toolState.isActiveHandle(p)) {
				active = p;
			} else {
				_cropDot.drawAt(g2, p.getX(), p.getY());
			}
		}
		if (active != null) {
			_cropDotActive.drawAt(g2, active.getX(), active.getY());
		}

		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	public void setPreview(final boolean b) {
		_preview = b;
	}
}
