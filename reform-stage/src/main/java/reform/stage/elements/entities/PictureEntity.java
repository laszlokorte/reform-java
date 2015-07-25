package reform.stage.elements.entities;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.PictureForm;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vector;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.PivotPair;
import reform.stage.elements.outline.EntityOutline;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class PictureEntity implements Entity
{
	private final Identifier<? extends PictureForm> _formId;

	private final EntityPoint _topLeft;
	private final EntityPoint _bottomLeft;
	private final EntityPoint _topRight;
	private final EntityPoint _bottomRight;

	private final EntityPoint _top;
	private final EntityPoint _right;
	private final EntityPoint _bottom;
	private final EntityPoint _left;

	private final EntityPoint _center;

	private final Handle _topLeftHandle;
	private final Handle _bottomLeftHandle;
	private final Handle _topRightHandle;
	private final Handle _bottomRightHandle;

	private final Handle _topHandle;
	private final Handle _rightHandle;
	private final Handle _bottomHandle;
	private final Handle _leftHandle;

	private final EntityOutline _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

	private String _label = "Rectangle";

	private boolean _isGuide = false;

	public PictureEntity(final Identifier<? extends PictureForm> formId)
	{
		_formId = formId;
		_topLeft = new EntityPoint(formId, PictureForm.Point.TopLeft);
		_bottomLeft = new EntityPoint(formId, PictureForm.Point.BottomLeft);
		_topRight = new EntityPoint(formId, PictureForm.Point.TopRight);
		_bottomRight = new EntityPoint(formId, PictureForm.Point.BottomRight);

		_top = new EntityPoint(formId, PictureForm.Point.Top);
		_right = new EntityPoint(formId, PictureForm.Point.Right);
		_bottom = new EntityPoint(formId, PictureForm.Point.Bottom);
		_left = new EntityPoint(formId, PictureForm.Point.Left);

		_center = new EntityPoint(formId, PictureForm.Point.Center);

		_points.add(_topLeft);
		_points.add(_topRight);
		_points.add(_bottomLeft);
		_points.add(_bottomRight);
		_points.add(_top);
		_points.add(_left);
		_points.add(_right);
		_points.add(_bottom);
		_points.add(_center);

		_topLeftHandle = new Handle(formId, PictureForm.Point.TopLeft,
		                            PictureForm.Anchor.TopLeft,
		                            new PivotPair(_bottomRight, _center));

		_topRightHandle = new Handle(formId, PictureForm.Point.TopRight,
		                             PictureForm.Anchor.TopRight,
		                             new PivotPair(_bottomLeft, _center));

		_bottomLeftHandle = new Handle(formId, PictureForm.Point.BottomLeft,
		                               PictureForm.Anchor.BottomLeft,
		                               new PivotPair(_topRight, _center));

		_bottomRightHandle = new Handle(formId, PictureForm.Point.BottomRight,
		                                PictureForm.Anchor.BottomRight,
		                                new PivotPair(_topLeft, _center));

		_leftHandle = new Handle(formId, PictureForm.Point.Left, PictureForm.Anchor.Left,
		                         new PivotPair(_right, _center));

		_rightHandle = new Handle(formId, PictureForm.Point.Right,
		                          PictureForm.Anchor.Right,
		                          new PivotPair(_left, _center));

		_topHandle = new Handle(formId, PictureForm.Point.Top, PictureForm.Anchor.Top,
		                        new PivotPair(_bottom, _center));

		_bottomHandle = new Handle(formId, PictureForm.Point.Bottom,
		                           PictureForm.Anchor.Bottom,
		                           new PivotPair(_top, _center));

		_handles.add(_topHandle);
		_handles.add(_bottomHandle);
		_handles.add(_leftHandle);
		_handles.add(_rightHandle);
		_handles.add(_topLeftHandle);
		_handles.add(_bottomLeftHandle);
		_handles.add(_topRightHandle);
		_handles.add(_bottomRightHandle);

		_outline = new EntityOutline.Null(formId);
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

		_topLeftHandle.updateForRuntime(runtime, analyzer);
		_bottomLeftHandle.updateForRuntime(runtime, analyzer);
		_topRightHandle.updateForRuntime(runtime, analyzer);
		_bottomRightHandle.updateForRuntime(runtime, analyzer);

		_topHandle.updateForRuntime(runtime, analyzer);
		_rightHandle.updateForRuntime(runtime, analyzer);
		_bottomHandle.updateForRuntime(runtime, analyzer);
		_leftHandle.updateForRuntime(runtime, analyzer);

		_shape.reset();
		_shape.moveTo(_topLeft.getX(), _topLeft.getY());
		_shape.lineTo(_topRight.getX(), _topRight.getY());
		_shape.lineTo(_bottomRight.getX(), _bottomRight.getY());
		_shape.lineTo(_bottomLeft.getX(), _bottomLeft.getY());
		_shape.closePath();

		final Form form = runtime.get(_formId);
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

		return Vector.isInTriangle(position.x, position.y, _topLeft.getX(),
		                           _topLeft.getY(), _bottomLeft.getX(),
		                           _bottomLeft.getY(), _bottomRight.getX(),
		                           _bottomRight.getY()) || Vector.isInTriangle
				(position.x, position.y,
		                                                                       _topRight
						 .getX(),
		                                                                       _topRight
						 .getY(),
		                                                                       _bottomLeft.getX(),
		                                                                       _bottomLeft.getY(),
		                                                                       _bottomRight.getX(),
		                                                                       _bottomRight.getY());
	}

	@Override
	public Identifier<? extends PictureForm> getId()
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
