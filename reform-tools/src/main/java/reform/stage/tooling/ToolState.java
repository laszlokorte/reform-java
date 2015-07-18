package reform.stage.tooling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import reform.math.Vec2;
import reform.stage.elements.CropPoint;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.elements.outline.IntersectionSnapPoint;

public class ToolState {
	private final List<ToolStateListener> _listeners = new ArrayList<>();

	private final List<SnapPoint> _visibleSnapPoints = new ArrayList<>();
	private final List<EntityPoint> _entityPoints = new ArrayList<>();
	private final List<Handle> _handles = new ArrayList<>();
	private SnapPoint _activeSnapPoint;
	private SnapPoint _activeEntityPoint;

	private Vec2 _pivot;

	private Handle _activeHandle;

	private boolean _selectionVisible;

	private String _description;

	private CropPoint _activeCropPoint;

	private boolean _cropPointsVisisble;

	private boolean _preview;

	public void setSnapPoints(final Collection<SnapPoint> points) {
		_visibleSnapPoints.clear();
		_visibleSnapPoints.addAll(points);
		notifyChange();
	}

	public void clearSnapPoints() {
		_visibleSnapPoints.clear();
		notifyChange();
	}

	public void clearHandles() {
		_handles.clear();
		notifyChange();
	}

	public void setActiveSnapPoint(final SnapPoint p) {
		_activeSnapPoint = p;
		notifyChange();
	}

	public boolean isActiveSnapPoint(final SnapPoint p) {
		return _activeSnapPoint != null && _activeSnapPoint.equals(p);
	}

	public void setActiveEntityPoint(final SnapPoint p) {
		_activeEntityPoint = p;
		notifyChange();
	}

	public boolean isActiveEntityPoint(final SnapPoint p) {
		return _activeEntityPoint == p;
	}

	public boolean belongsToActiveSnapPoint(final Entity e) {
		if (_activeSnapPoint instanceof EntityPoint) {
			return ((EntityPoint) _activeSnapPoint).getFormId()
					.equals(e.getId());
		}

		if (_activeSnapPoint instanceof IntersectionSnapPoint) {
			return ((IntersectionSnapPoint) _activeSnapPoint).getFormIdA()
					.equals(e.getId())
					|| ((IntersectionSnapPoint) _activeSnapPoint).getFormIdB()
							.equals(e.getId());
		}

		return false;
	}

	public void setActiveCropPoint(final CropPoint p) {
		_activeCropPoint = p;
		notifyChange();
	}

	public boolean isActiveCropPoint(final CropPoint p) {
		return _activeCropPoint == p;
	}

	public void setCropPointsVisible(final boolean value) {
		_cropPointsVisisble = value;
	}

	public boolean areCropPointsVisible() {
		return _cropPointsVisisble;
	}

	public void addListener(final ToolStateListener l) {
		_listeners.add(l);
	}

	public void removeListener(final ToolStateListener l) {
		_listeners.remove(l);
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(final String description) {
		_description = description;
		notifyChange();
	}

	private void notifyChange() {
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onToolStateChange(this);
		}
	}

	public List<SnapPoint> getSnapPoints() {
		return _visibleSnapPoints;
	}

	public List<EntityPoint> getEntityPoints() {
		return _entityPoints;
	}

	public boolean isSelectionVisible() {
		return _selectionVisible;
	}

	public void setSelectionVisible(final boolean newValue) {
		if (newValue != _selectionVisible) {
			_selectionVisible = newValue;
			notifyChange();
		}
	}

	public void setActiveHandle(final Handle handle) {
		if (handle != _activeHandle) {
			_activeHandle = handle;
			notifyChange();
		}
	}

	public boolean isActiveHandle(final Handle handle) {
		return _activeHandle == handle;
	}

	public void setEntityPoints(final Collection<EntityPoint> points) {
		_entityPoints.clear();
		_entityPoints.addAll(points);
		notifyChange();
	}

	public void clearEntityPoints() {
		_entityPoints.clear();
		notifyChange();
	}

	public void setPivot(final Vec2 pivot) {
		_pivot = pivot;
		notifyChange();
	}

	public Vec2 getPivot() {
		return _pivot;
	}

	public void setHandles(final Collection<Handle> handles) {
		_handles.clear();
		_handles.addAll(handles);
		notifyChange();
	}

	public List<Handle> getHandles() {
		return _handles;
	}

	public boolean isPreviewMode() {
		return _preview;
	}

	public void setPreviewMode(final boolean enabled) {
		_preview = enabled;
		notifyChange();
	}

}
