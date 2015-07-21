package reform.playground.renderers;

import reform.math.Vec2i;
import reform.rendering.assets.Asset;
import reform.rendering.assets.Dot;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.ControlPoint;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.RubberBand;

import java.util.List;
import java.awt.*;

public class InstructionControlRenderer implements CanvasRenderer
{
	private final Stage _stage;

	private final Stroke _rubberStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 4, new float[] {4,4} , 4);
	private final Color _rubberColor = new Color(0x7AC3D2);
	private final Asset _errorDot = new Dot(11, 3, new Color(0xE56E6B), new Color(0xA60B07));
	private final Asset _markerDot = new Dot(8, 3, new Color(0xBBE8EE), new Color(0xBBE8EE));

	private boolean _preview;

	public InstructionControlRenderer(final Stage stage)
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

		InstructionControl control = _stage.getInstructionControl();
		if (control != null && control.canEdit())
		{
			List<ControlPoint> controlPoints = control.getControlPoints();
			RubberBand rubber = control.getRubberBand();

			if(rubber != null) {
				g2.setStroke(_rubberStroke);
				g2.setColor(_rubberColor);
				g2.drawLine((int)rubber.getStartX(), (int)rubber.getStartY(), (int)rubber.getEndX(), (int)rubber.getEndY());
			}

			for (int i = 0, j = controlPoints.size(); i < j; i++)
			{
				ControlPoint p = controlPoints.get(i);
				if(p.hasError()) {
					_errorDot.drawAt(g2, p.getX(), p.getY());
				} else {
					_markerDot.drawAt(g2, p.getX(), p.getY());
				}
			}
		}

		g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}
}
