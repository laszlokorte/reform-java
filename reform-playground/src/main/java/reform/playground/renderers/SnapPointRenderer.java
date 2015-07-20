package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.Entity;
import reform.stage.elements.SnapPoint;
import reform.stage.tooling.ToolState;

import java.awt.*;
import java.util.List;

public class SnapPointRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;

	private final Stroke _borderStroke = new BasicStroke(4);
	private final Color _borderColor = new Color(0x99E3B424, true);
	private final Asset _cropDot = new Dot(5, 1, new Color(0xE3B424), new Color(0xE3B424));

	private final Asset _cropDotActive = new Dot(7, 1, new Color(0xE3B424), new Color(0xE3B424));
	private boolean _preview;

	public SnapPointRenderer(final Stage stage, final ToolState toolState)
	{
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{

		final ToolState.ViewState viewState = _toolState.getViewState();
		if (viewState == ToolState.ViewState.Snap || viewState == ToolState.ViewState.SnapEntity || viewState ==
				ToolState.ViewState.SnapHandle)
		{

			final Vec2i size = _stage.getSize();

			g2.translate((width - size.x) / 2, (height - size.y) / 2);

			SnapPoint active = null;
			if (_preview)
			{
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			}

			final List<Entity> entities = _stage.getEntities();
			for (int i = 0, j = entities.size(); i < j; i++)
			{
				final Entity entity = entities.get(i);
				if (_toolState.belongsToActiveSnapPoint(entity))
				{
					g2.setStroke(_borderStroke);
					g2.setColor(_borderColor);
					g2.draw(entity.getShape());
				}
			}

			final List<SnapPoint> snapPoints = _toolState.getSnapPoints();
			for (int i = 0, j = snapPoints.size(); i < j; i++)
			{
				final SnapPoint p = snapPoints.get(i);
				if (_toolState.isActiveSnapPoint(p))
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
