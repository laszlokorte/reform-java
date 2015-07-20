package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.SnapPoint;
import reform.stage.tooling.ToolState;

import java.awt.*;
import java.util.List;

public class EntityPointRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;

	private final Asset _cropDot = new Dot(6, 2, new Color(0x3192F2), new Color(0x3192F2));

	private final Asset _cropDotActive = new Dot(7, 1, new Color(0x3192F2), new Color(0x3192F2).darker());
	private boolean _preview;

	public EntityPointRenderer(final Stage stage, final ToolState toolState)
	{
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{
		final ToolState.ViewState viewState = _toolState.getViewState();
		if (viewState == ToolState.ViewState.EntityPoint || viewState == ToolState.ViewState.SnapEntity)
		{

			final Vec2i size = _stage.getSize();

			g2.translate((width - size.x) / 2, (height - size.y) / 2);

			SnapPoint active = null;
			if (_preview)
			{
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			}

			final List<EntityPoint> entityPoints = _toolState.getEntityPoints();
			for (int i = 0, j = entityPoints.size(); i < j; i++)
			{
				final EntityPoint p = entityPoints.get(i);
				if (_toolState.isActiveEntityPoint(p))
				{
					active = p;
				}
				else
				{
					_cropDot.drawAt(g2, p.getX(), p.getY());
				}
			}
			if (active != null)
			{
				_cropDotActive.drawAt(g2, active.getX(), active.getY());
			}

			g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
		}
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}
}
