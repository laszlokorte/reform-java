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
import reform.stage.elements.Entity;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.ToolState;

public class SelectionRenderer implements CanvasRenderer {

	private final Stage _stage;
	private final FormSelection _formSelection;
	private final ToolState _toolState;

	private final Stroke _stroke = new BasicStroke(3, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND);
	private final Color _glowColorCurrent = new Color(0x6600aaff, true);
	private final Color _backgroundColorCurrent = new Color(0x1100aaff, true);
	private final Color _glowColorAssoc = new Color(0x6600ddaa, true);

	private boolean _preview;

	public SelectionRenderer(final Stage stage,
			final FormSelection formSelection, final ToolState toolState) {
		_stage = stage;
		_formSelection = formSelection;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		final Vec2i size = _stage.getSize();

		g2.translate((width - size.x) / 2, (height - size.y) / 2);

		renderElements(g2);
		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	private void renderElements(final Graphics2D g2) {
		if (_toolState.getState() != ToolState.State.Preview && _toolState
                .getState() != ToolState.State.Crop) {
			g2.setStroke(_stroke);
			if (_preview) {
				g2.setColor(_glowColorAssoc);
				final List<Shape> shapes = _stage.getFinalShapes();
				for (int i = 0, j = shapes.size(); i < j; i++) {
					final Shape s = shapes.get(i);
					if (_stage.getIdFor(s)
							.equals(_formSelection.getSelected())) {
						g2.draw(s);
					}
				}
			}

			{
				final List<Entity> entities = _stage.getEntities();
				for (int i = 0, j = entities.size(); i < j; i++) {
					final Entity e = entities.get(i);
					if (e.getId().equals(_formSelection.getSelected())) {
						final Shape s = e.getShape();
						g2.setColor(_glowColorCurrent);
						g2.draw(s);
						if (!e.isGuide()) {
							g2.setColor(_backgroundColorCurrent);
							g2.fill(s);
						}
					}
				}
			}
		}
	}

	public void setPreview(final boolean b) {
		_preview = b;
	}

}
