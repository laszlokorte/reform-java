package reform.playground.renderers;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.rendering.canvas.CanvasRenderer;
import reform.stage.Stage;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.tooling.FormSelection;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

import java.awt.*;

public class ToolStateDescriptionRenderer implements CanvasRenderer
{

	private final Stage _stage;
	private final ToolState _toolState;
	private final Analyzer _analyzer;
	private final FormSelection _selection;
	private final Cursor _cursor;

	private final ToolTipRenderer _toolTipRenderer;

	private boolean _preview;
	private final Color _snapTextColor = new Color(0xFFD88A);
	private final Color _handleTextColor = new Color(0x97FFAF);
	private final Color _pointTextColor = new Color(0xBBECFF);
	private final Color _selectionTextColor = new Color(0xDCDCDC);

	public ToolStateDescriptionRenderer(final Stage stage, final ToolState toolState, final FormSelection selection,
	                                    final Analyzer analyzer, final ToolTipRenderer toolTipRenderer, final Cursor
			                                    cursor)
	{
		_stage = stage;
		_toolState = toolState;
		_analyzer = analyzer;
		_selection = selection;
		_toolTipRenderer = toolTipRenderer;
		_cursor = cursor;
	}

	@Override
	public void render(final Graphics2D g2, final int width, final int height)
	{
		final Vec2i size = _stage.getSize();

		if (!_preview && _toolState.getViewState() != ToolState.ViewState.Preview && _cursor.isCycled())
		{
			g2.translate((width - size.x) / 2, (height - size.y) / 2);

			final ToolState.SelectionState selectionState = _toolState.getSelectionState();
			if (selectionState == ToolState.SelectionState.SnapPoint)
			{
				final SnapPoint snap = _toolState.getActiveSnapPoint();
				if (snap != null)
				{
					_toolTipRenderer.render(g2, snap.getX(), snap.getY(), snap.getLabel(), _snapTextColor);

				}
			}
			else if (selectionState == ToolState.SelectionState.Handle)
			{
				final Handle handle = _toolState.getActiveHandle();
				if (handle != null)
				{
					_toolTipRenderer.render(g2, handle.getX(), handle.getY(), handle.getLabel(), _handleTextColor);

				}
			}
			else if (selectionState == ToolState.SelectionState.EntityPoint)
			{
				final EntityPoint point = _toolState.getActiveEntityPoint();
				if (point != null)
				{
					_toolTipRenderer.render(g2, point.getX(), point.getY(), point.getLabel(), _pointTextColor);

				}
			}
			else if (selectionState == ToolState.SelectionState.Form)
			{
				final Identifier<? extends Form> formId = _selection.getSelected();
				if (formId != null)
				{
					final String formName = _analyzer.getForm(formId).getName().getValue();
					_toolTipRenderer.renderCentered(g2, size.x / 2, size.y / 2, formName, _selectionTextColor);
				}
			}


			g2.translate(-(width - size.x) / 2, -(height - size.y) / 2);

		}
	}

	public void setPreview(final boolean b)
	{
		_preview = b;
	}
}
