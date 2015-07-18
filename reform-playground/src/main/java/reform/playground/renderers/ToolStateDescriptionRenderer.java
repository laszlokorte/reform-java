package reform.playground.renderers;

import reform.core.analyzer.Analyzer;
import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.ToolState;

import java.awt.*;

public class ToolStateDescriptionRenderer implements CanvasRenderer {

	private final Stage _stage;
	private final ToolState _toolState;
	private final Analyzer _analyzer;
	private final FormSelection _selection;


	private final Asset _cropDot = new Dot(6, 2, new Color(0xB14BEC),
			new Color(0xB14BEC));

	private boolean _preview;
	private final Color _backgroundColor = new Color(0x88333333, true);
    private final Color _selectionTextColor = new Color(0xffffff);

    public ToolStateDescriptionRenderer(final Stage stage, final ToolState toolState, final FormSelection selection, final Analyzer analyzer) {
		_stage = stage;
		_toolState = toolState;
		_analyzer = analyzer;
		_selection = selection;

	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height) {
		final Vec2i size = _stage.getSize();

		if(! _preview && _toolState.getState() != ToolState.State.Preview) {
			g2.setColor(_backgroundColor);
			if((_toolState.getState() == ToolState.State.Edit || _toolState
                    .getState() == ToolState.State.Select)) {
                SnapPoint snap = _toolState.getActiveSnapPoint();
                Handle handle = _toolState.getActiveHandle();
                EntityPoint entityPoint = _toolState.getActiveEntityPoint();
                if(snap != null) {
                    g2.fillRect(0, height - 25, width, 25);
                    g2.setColor(_selectionTextColor);
                    g2.drawString("Snap to...", 10, height - 7);
                } else if(entityPoint != null) {
                    g2.fillRect(0, height - 25, width, 25);
                    g2.setColor(_selectionTextColor);
                    g2.drawString("Grab...", 10, height - 7);
                } else if(handle != null) {
                    g2.fillRect(0, height - 25, width, 25);
                    g2.setColor(_selectionTextColor);
                    g2.drawString("Grab...", 10, height - 7);
                } else if( _selection
                        .isSet
                                ()) {
                    g2.fillRect(0, height - 25, width, 25);
                    g2.setColor(_selectionTextColor);
                    g2.drawString("...is selected", 10, height - 7);
                }
            } else if(_toolState.getState() == ToolState.State.Crop) {
                g2.fillRect(0, height - 25, width, 25);
                g2.setColor(_selectionTextColor);
                g2.drawString("Adjust canvas size...", 10, height - 7);
            } else if(_toolState.getState() == ToolState.State.Create) {
                SnapPoint snap = _toolState.getActiveSnapPoint();
                if(snap != null) {
                    g2.fillRect(0, height - 25, width, 25);
                    g2.setColor(_selectionTextColor);
                    g2.drawString("Snap to...", 10, height - 7);
                }
            } else if(_toolState.getState() == ToolState.State.Preview) {

            }
		}
	}

	public void setPreview(final boolean b) {
		_preview = b;
	}
}
