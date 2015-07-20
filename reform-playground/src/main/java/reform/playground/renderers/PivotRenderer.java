package reform.playground.renderers;

import reform.math.Vec2;
import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.tooling.ToolState;

import java.awt.*;

public class PivotRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;

	private final Asset _cropDot = new Dot(6, 2, new Color(0xB14BEC), new Color(0xB14BEC));

	private boolean _preview;

	public PivotRenderer(final Stage stage, final ToolState toolState)
	{
		_stage = stage;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{
		final Vec2i size = _stage.getSize();

		g2.translate((width - size.x) / 2, (height - size.y) / 2);

		if (_preview)
		{
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		}

		final Vec2 pivot = _toolState.getPivot();
		if (pivot != null)
		{
			_cropDot.drawAt(g2, pivot.x, pivot.y);
		}

		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}
}
