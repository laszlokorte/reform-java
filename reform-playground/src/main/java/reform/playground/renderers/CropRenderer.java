package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.CropPoint;
import reform.stage.tooling.ToolState;

import java.awt.*;
import java.util.List;

public class CropRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;

	private final Stroke _borderStroke = new BasicStroke(2);
	private final Color _borderColor = new Color(0x2f7f3f);
	private final Asset _cropDot = new Dot(8, 2, new Color(0x30b040), new Color(0x2f7f3f));

	private final Asset _cropDotActive = new Dot(9, 3, new Color(0x45C556), new Color(0x1f6f2f));

	public CropRenderer(final Stage stage, final ToolState toolState)
	{
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{

		if (_toolState.getViewState() == ToolState.ViewState.Crop)
		{
			final Vec2i size = _stage.getSize();

			g2.translate((width - size.x) / 2, (height - size.y) / 2);

			CropPoint active = null;
			g2.setStroke(_borderStroke);
			g2.setColor(_borderColor);
			g2.drawRect(0, 0, size.x, size.y);
			final List<CropPoint> cropPoints = _stage.getCropPoints();
			for (int i = 0, j = cropPoints.size(); i < j; i++)
			{
				final CropPoint p = cropPoints.get(i);
				if (_toolState.isActiveCropPoint(p))
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
}
