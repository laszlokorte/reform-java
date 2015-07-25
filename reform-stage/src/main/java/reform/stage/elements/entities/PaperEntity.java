package reform.stage.elements.entities;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.procedure.Paper;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.outline.EntityOutline;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class PaperEntity implements Entity
{
	private final Identifier<? extends Paper> _formId;

	private final EntityPoint _topLeft;
	private final EntityPoint _bottomLeft;
	private final EntityPoint _topRight;
	private final EntityPoint _bottomRight;

	private final EntityPoint _top;
	private final EntityPoint _right;
	private final EntityPoint _bottom;
	private final EntityPoint _left;

	private final EntityPoint _center;

	private final EntityOutline _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

	private String _label = "Paper";

	public PaperEntity(final Identifier<? extends Paper> formId)
	{
		_formId = formId;
		_topLeft = new EntityPoint(formId, Paper.Point.TopLeft);
		_bottomLeft = new EntityPoint(formId, Paper.Point.BottomLeft);
		_topRight = new EntityPoint(formId, Paper.Point.TopRight);
		_bottomRight = new EntityPoint(formId, Paper.Point.BottomRight);

		_top = new EntityPoint(formId, Paper.Point.Top);
		_right = new EntityPoint(formId, Paper.Point.Right);
		_bottom = new EntityPoint(formId, Paper.Point.Bottom);
		_left = new EntityPoint(formId, Paper.Point.Left);

		_center = new EntityPoint(formId, Paper.Point.Center);

		_outline = new EntityOutline.Null(formId);

		_points.add(_topLeft);
		_points.add(_bottomLeft);
		_points.add(_topRight);
		_points.add(_bottomRight);
		_points.add(_top);
		_points.add(_right);
		_points.add(_bottom);
		_points.add(_left);
		_points.add(_center);
	}

	@Override
	public void updateForRuntime(final Runtime runtime, final Analyzer analyzer)
	{
		_topLeft.updateForRuntime(runtime, analyzer);
		_bottomLeft.updateForRuntime(runtime, analyzer);
		_topRight.updateForRuntime(runtime, analyzer);
		_bottomRight.updateForRuntime(runtime, analyzer);

		_center.updateForRuntime(runtime, analyzer);

		_top.updateForRuntime(runtime, analyzer);
		_right.updateForRuntime(runtime, analyzer);
		_bottom.updateForRuntime(runtime, analyzer);
		_left.updateForRuntime(runtime, analyzer);

		_shape.reset();
		_shape.moveTo(_topLeft.getX(), _topLeft.getY());
		_shape.lineTo(_topRight.getX(), _topRight.getY());
		_shape.lineTo(_bottomRight.getX(), _bottomRight.getY());
		_shape.lineTo(_bottomLeft.getX(), _bottomLeft.getY());
		_shape.closePath();

		final Form form = runtime.get(_formId);

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
		return _topLeft.getX() < position.x && _topLeft.getY() < position.y &&
				_bottomRight.getX() > position.x &&
				_bottomRight.getY() > position.y;
	}

	@Override
	public Identifier<? extends Paper> getId()
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
		return false;
	}


	@Override
	public String getLabel()
	{
		return _label;
	}


}
