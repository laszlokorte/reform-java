package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.Entity;
import reform.stage.tooling.ToolState;

import java.awt.*;
import java.util.List;

public class GuideRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;
	private final Stroke _stroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
	                                               BasicStroke.JOIN_ROUND);
	private final Color _guideColor = new Color(0x00ffff);
	private boolean _preview;

	public GuideRenderer(final Stage stage, final ToolState toolState)
	{
		_stage = stage;
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
		if (!_preview && _toolState.getViewState() != ToolState.ViewState.Preview)
		{
			g2.setStroke(_stroke);

			final List<Entity> entities = _stage.getEntities();
			for (int i = 0, j = entities.size(); i < j; i++)
			{
				final Entity e = entities.get(i);
				if (e.isGuide())
				{
					final Shape s = e.getShape();
					g2.setColor(_guideColor);
					g2.draw(s);
				}
			}
		}
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}

}
