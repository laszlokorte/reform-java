package reform.playground.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.List;

import reform.math.Vec2i;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.tooling.ToolState;

public class StageRenderer implements CanvasRenderer {

	private final Stage _stage;

	private final Stroke _stroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);
	private final Color _fillColor = new Color(0x99888888, true);
	private final Color _borderColor = new Color(0x333333, false);

	private boolean _preview;

	private final ToolState _toolState;

	public StageRenderer(final Stage stage, final ToolState toolState) {
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		final Vec2i size = _stage.getSize();

		g2.setColor(Color.WHITE);
		g2.fillRect((width - size.x) / 2, (height - size.y) / 2, size.x,
				size.y);

		g2.translate((width - size.x) / 2, (height - size.y) / 2);
		final Shape clip = g2.getClip();
		if (_preview || _toolState.isPreviewMode()) {
			g2.clipRect(0, 0, size.x, size.y);
		}
		renderElements(g2);
		g2.setClip(clip);
		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	private void renderElements(final Graphics2D g2) {
		if (_preview || _toolState.isPreviewMode()) {
			g2.setStroke(_stroke);

			final List<Shape> finalShapes = _stage.getFinalShapes();
			for (int i = 0, j = finalShapes.size(); i < j; i++) {
				final Shape s = finalShapes.get(i);
				g2.setColor(_fillColor);
				g2.fill(s);
				g2.setColor(_borderColor);
				g2.draw(s);
			}
		} else {
			g2.setStroke(_stroke);
			final List<Shape> currentShapes = _stage.getCurrentShapes();
			for (int i = 0, j = currentShapes.size(); i < j; i++) {
				final Shape s = currentShapes.get(i);
				g2.setColor(_fillColor);
				g2.fill(s);
				g2.setColor(_borderColor);
				g2.draw(s);
			}

		}

	}

	public void setPreview(final boolean b) {
		_preview = b;
	}

}
