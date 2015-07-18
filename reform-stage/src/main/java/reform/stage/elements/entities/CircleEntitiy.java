package reform.stage.elements.entities;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import reform.core.forms.CircleForm;
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

public class CircleEntitiy implements Entity {
	private final Identifier<? extends CircleForm> _formId;

	private final EntityPoint _top;
	private final EntityPoint _right;
	private final EntityPoint _bottom;
	private final EntityPoint _left;
	private final EntityPoint _center;

	private final Handle _topHandle;
	private final Handle _rightHandle;
	private final Handle _bottomHandle;
	private final Handle _leftHandle;

	private final EntityOutline.Circle _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

	private boolean _isGuide = false;

	public CircleEntitiy(final Identifier<? extends CircleForm> formId) {
		_formId = formId;
		_top = new EntityPoint(formId, CircleForm.Point.Top);
		_right = new EntityPoint(formId, CircleForm.Point.Right);
		_bottom = new EntityPoint(formId, CircleForm.Point.Bottom);
		_left = new EntityPoint(formId, CircleForm.Point.Left);
		_center = new EntityPoint(formId, CircleForm.Point.Center);

		_points.add(_top);
		_points.add(_right);
		_points.add(_bottom);
		_points.add(_left);
		_points.add(_center);

		_topHandle = new Handle(formId, CircleForm.Point.Top,
				CircleForm.Anchor.Top, new PivotPair(_center, _bottom));
		_bottomHandle = new Handle(formId, CircleForm.Point.Bottom,
				CircleForm.Anchor.Bottom, new PivotPair(_center, _top));
		_rightHandle = new Handle(formId, CircleForm.Point.Right,
				CircleForm.Anchor.Right, new PivotPair(_center, _left));
		_leftHandle = new Handle(formId, CircleForm.Point.Left,
				CircleForm.Anchor.Left, new PivotPair(_center, _right));

		_handles.add(_topHandle);
		_handles.add(_bottomHandle);
		_handles.add(_rightHandle);
		_handles.add(_leftHandle);

		_outline = new EntityOutline.Circle(formId);
	}

	@Override
	public void updateForRuntime(final Runtime runtime) {
		_top.updateForRuntime(runtime);
		_right.updateForRuntime(runtime);
		_bottom.updateForRuntime(runtime);
		_left.updateForRuntime(runtime);
		_center.updateForRuntime(runtime);

		_topHandle.updateForRuntime(runtime);
		_bottomHandle.updateForRuntime(runtime);
		_leftHandle.updateForRuntime(runtime);
		_rightHandle.updateForRuntime(runtime);

		_outline.update(_center.getX(), _center.getY(), Vector.distance(
				_center.getX(), _center.getY(), _top.getX(), _top.getY()));

		_shape.reset();
		runtime.get(_formId).appendToPathForRuntime(runtime, _shape);

		_isGuide = runtime.get(_formId).getType() == DrawingType.Guide;

	}

	@Override
	public List<EntityPoint> getSnapPoints() {
		return _points;
	}

	@Override
	public List<Handle> getHandles() {
		return _handles;
	}

	@Override
	public boolean contains(final Vec2 position) {
		return _shape.contains(position.x, position.y);
	}

	@Override
	public Identifier<? extends CircleForm> getId() {
		return _formId;
	}

	@Override
	public EntityOutline getOutline() {
		return _outline;
	}

	@Override
	public Shape getShape() {
		return _shape;
	}

	@Override
	public boolean isGuide() {
		return _isGuide;
	}
}
