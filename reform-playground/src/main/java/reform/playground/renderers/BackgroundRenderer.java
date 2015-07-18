package reform.playground.renderers;

import java.awt.Color;
import java.awt.Graphics2D;

import reform.rendering.canvas.CanvasRenderer;
import reform.stage.tooling.ToolState;

public class BackgroundRenderer implements CanvasRenderer {

	private final Color _colorLight;
	private final Color _colorDark;

	private boolean _isPreview = false;
	private final ToolState _toolState;

	public BackgroundRenderer(final ToolState toolState, final Color colorLight,
			final Color colorDark) {
		_colorLight = colorLight;
		_colorDark = colorDark;
		_toolState = toolState;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		if (_isPreview || _toolState.getState() == ToolState.State.Preview) {
			g2.setColor(_colorDark);

		} else {
			g2.setColor(_colorLight);
		}
		g2.fillRect(0, 0, width, height);
	}

	public void setPreview(final boolean enabled) {
		_isPreview = enabled;
	}

}
