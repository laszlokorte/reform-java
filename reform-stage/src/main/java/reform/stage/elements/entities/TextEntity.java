package reform.stage.elements.entities;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.TextForm;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.PivotPair;
import reform.stage.elements.outline.EntityOutline;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class TextEntity implements Entity
{
	private final Identifier<? extends TextForm> _formId;

	private final EntityPoint _start;
	private final EntityPoint _end;
	private final EntityPoint _top;
	private final EntityPoint _bottom;

	private final Handle _startHandle;
	private final Handle _endHandle;
	private final Handle _topHandle;

	private final EntityOutline.Line _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

	private String _label = "Text";

	private boolean _isGuide = false;

	public TextEntity(final Identifier<? extends TextForm> formId)
	{
		_formId = formId;

		_start = new EntityPoint(formId, TextForm.Point.Start);
		_end = new EntityPoint(formId, TextForm.Point.End);
		_top = new EntityPoint(formId, TextForm.Point.Top);
		_bottom = new EntityPoint(formId, TextForm.Point.Bottom);

		_points.add(_start);
		_points.add(_end);
		_points.add(_top);
		_points.add(_bottom);

		_startHandle = new Handle(formId, TextForm.Point.Start, TextForm.Anchor.Start, new PivotPair(_end, _bottom));
		_endHandle = new Handle(formId, TextForm.Point.End, TextForm.Anchor.End, new PivotPair(_start, _bottom));
		_topHandle = new Handle(formId, TextForm.Point.Top, TextForm.Anchor.Top);

		_handles.add(_startHandle);
		_handles.add(_endHandle);
		_handles.add(_topHandle);

		_outline = new EntityOutline.Line(formId);

	}

	@Override
	public void updateForRuntime(final Runtime runtime, final Analyzer analyzer)
	{
		_start.updateForRuntime(runtime, analyzer);
		_end.updateForRuntime(runtime, analyzer);
		_top.updateForRuntime(runtime, analyzer);
		_bottom.updateForRuntime(runtime, analyzer);

		_outline.update(_start.getX(), _start.getY(), _end.getX(), _end.getY());

		_startHandle.updateForRuntime(runtime, analyzer);
		_endHandle.updateForRuntime(runtime, analyzer);
		_topHandle.updateForRuntime(runtime, analyzer);

		_shape.reset();
		final Form form = runtime.get(_formId);
		form.appendToPathForRuntime(runtime, _shape);

		_label = form.getName().getValue();

		_isGuide = runtime.get(_formId).getType() == DrawingType.Guide;

	}

	@Override
	public List<EntityPoint> getSnapPoints()
	{
		return _points;
	}

	@Override
	public List<Handle> getHandles()
	{
		return _handles;
	}

	@Override
	public boolean contains(final Vec2 position)
	{
		return _shape.intersects(position.x - 5, position.y - 5, 10, 10);
	}

	@Override
	public Identifier<? extends TextForm> getId()
	{
		return _formId;
	}

	@Override
	public EntityOutline getOutline()
	{
		return _outline;
	}

	@Override
	public Shape getShape()
	{
		return _shape;
	}

	@Override
	public boolean isGuide()
	{
		return _isGuide;
	}


	@Override
	public String getLabel()
	{
		return _label;
	}

}
