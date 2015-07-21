package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.errors.Marker;
import reform.stage.tooling.ToolState;

import java.awt.*;

public class ErrorMarkerRenderer implements CanvasRenderer
{
	private final Stage _stage;

	private final Asset _errorDot = new Dot(11, 3, new Color(0x99A60B07,true), new Color(0xA60B07));

	private boolean _preview;

	public ErrorMarkerRenderer(final Stage stage)
	{
		_stage = stage;
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

		final Marker marker = _stage.getErrorMarker();
		if (marker != null)
		{
			_errorDot.drawAt(g2, marker.getPosition().x, marker.getPosition().y);
		}

		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}
}
