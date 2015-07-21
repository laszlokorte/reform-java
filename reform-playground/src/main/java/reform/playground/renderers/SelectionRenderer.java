package reform.playground.renderers;

import reform.core.graphics.ColoredShape;
import reform.math.Vec2i;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.Entity;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.ToolState;

import java.awt.*;
import java.util.List;

public class SelectionRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final FormSelection _formSelection;
	private final ToolState _toolState;

	private final Stroke _stroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final Color _glowColorCurrent = new Color(0x6600aaff, true);
	private final Color _backgroundColorCurrent = new Color(0x1100aaff, true);
	private final Color _glowColorAssoc = new Color(0x6600ddaa, true);

	private boolean _preview;

	public SelectionRenderer(final Stage stage, final FormSelection formSelection, final ToolState toolState)
	{
		_stage = stage;
		_formSelection = formSelection;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{
		final Vec2i size = _stage.getSize();

		g2.translate((width - size.x) / 2, (height - size.y) / 2);

		renderElements(g2);
		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	private void renderElements(final Graphics2D g2)
	{
		ToolState.ViewState viewState = _toolState.getViewState();
		if (viewState!= ToolState.ViewState.Preview && viewState != ToolState
				.ViewState.Crop && viewState != ToolState.ViewState.Control)
		{
			g2.setStroke(_stroke);
			if (_preview)
			{
				g2.setColor(_glowColorAssoc);
				final List<ColoredShape> shapes = _stage.getFinalShapes();
				for (int i = 0, j = shapes.size(); i < j; i++)
				{
					final ColoredShape s = shapes.get(i);
					if (_stage.getIdFor(s).equals(_formSelection.getSelected()))
					{
						g2.draw(s.getPath());
					}
				}
			}

			{
				final List<Entity> entities = _stage.getEntities();
				for (int i = 0, j = entities.size(); i < j; i++)
				{
					final Entity e = entities.get(i);
					if (e.getId().equals(_formSelection.getSelected()))
					{
						final Shape s = e.getShape();
						g2.setColor(_glowColorCurrent);
						g2.draw(s);
						if (!e.isGuide() && _preview)
						{
							g2.setColor(_backgroundColorCurrent);
							g2.fill(s);
						}
					}
				}
			}
		}
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}

}
