package reform.stage.elements.entities;

import reform.core.analyzer.Analyzer;
import reform.core.forms.ArcForm;
import reform.core.forms.Form;
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

public class ArcEntity implements Entity
{
	private final Identifier<? extends ArcForm> _formId;

	private final EntityPoint _start;
	private final EntityPoint _end;
	private final EntityPoint _center;

	private final Handle _startHandle;
	private final Handle _endHandle;
	private final Handle _centerHandle;

	private final EntityOutline _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

	private String _label = "Arc";

	private boolean _isGuide = false;

	public ArcEntity(final Identifier<? extends ArcForm> formId)
	{
		_formId = formId;
		_start = new EntityPoint(formId, ArcForm.Point.Start);
		_end = new EntityPoint(formId, ArcForm.Point.End);
		_center = new EntityPoint(formId, ArcForm.Point.Center);

		_points.add(_start);
		_points.add(_end);
		_points.add(_center);

		_startHandle = new Handle(formId, ArcForm.Point.Start, ArcForm.Anchor.Start,
		                          new PivotPair(_center, _end));
		_endHandle = new Handle(formId, ArcForm.Point.End, ArcForm.Anchor.End,
		                        new PivotPair(_center, _start));
		_centerHandle = new Handle(formId, ArcForm.Point.Center, ArcForm.Anchor.Center);

		_handles.add(_startHandle);
		_handles.add(_endHandle);
		_handles.add(_centerHandle);

		_outline = new EntityOutline.Null(formId);

	}

	@Override
	public void updateForRuntime(final Runtime runtime, final Analyzer analyzer)
	{
		_start.updateForRuntime(runtime, analyzer);
		_end.updateForRuntime(runtime, analyzer);
		_center.updateForRuntime(runtime, analyzer);

		_startHandle.updateForRuntime(runtime, analyzer);
		_endHandle.updateForRuntime(runtime, analyzer);
		_centerHandle.updateForRuntime(runtime, analyzer);

		_shape.reset();
		final Form form = runtime.get(_formId);
		form.appendToPathForRuntime(runtime, _shape);

		_isGuide = runtime.get(_formId).getType() == DrawingType.Guide;

		_label = form.getName().getValue();
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
		return _shape.contains(position.x, position.y);
	}

	@Override
	public Identifier<? extends ArcForm> getId()
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
