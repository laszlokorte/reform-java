package reform.stage.tooling;

import reform.math.Vec2;
import reform.stage.elements.*;
import reform.stage.elements.outline.IntersectionSnapPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToolState
{

	public enum ViewState
	{
		Preview, Selection, Snap, SnapEntity, Handle, EntityPoint, SnapHandle, Crop, Control, SnapControl
	}

	public enum SelectionState
	{
		None, Form, Handle, EntityPoint, SnapPoint, CropPoint, Control
	}

	private final List<ToolStateListener> _listeners = new ArrayList<>();

	private final List<SnapPoint> _visibleSnapPoints = new ArrayList<>();
	private final List<EntityPoint> _entityPoints = new ArrayList<>();
	private final List<Handle> _handles = new ArrayList<>();
	private SnapPoint _activeSnapPoint;
	private EntityPoint _activeEntityPoint;
	private Handle _activeHandle;
	private CropPoint _activeCropPoint;

	private Vec2 _pivot;

	private String _description;

	private ViewState _viewState = ViewState.Selection;
	private SelectionState _selectionState = SelectionState.None;

	public void setViewState(final ViewState viewState)
	{
		if (_viewState != viewState)
		{
			_viewState = viewState;
			notifyChange();
		}
	}

	public ViewState getViewState()
	{
		return _viewState;
	}

	public void setSelectionState(final SelectionState selectionState)
	{
		if (_selectionState != selectionState)
		{
			_selectionState = selectionState;
			notifyChange();
		}
	}

	public SelectionState getSelectionState()
	{
		return _selectionState;
	}

	public void setSnapPoints(final Collection<SnapPoint> points)
	{
		_visibleSnapPoints.clear();
		_visibleSnapPoints.addAll(points);
		notifyChange();
	}

	public void clearSnapPoints()
	{
		_visibleSnapPoints.clear();
		notifyChange();
	}

	public void clearHandles()
	{
		_handles.clear();
		notifyChange();
	}

	public void setActiveSnapPoint(final SnapPoint p)
	{
		_activeSnapPoint = p;
		notifyChange();
	}

	public boolean isActiveSnapPoint(final SnapPoint p)
	{
		return _activeSnapPoint != null && _activeSnapPoint.equals(p);
	}

	public void setActiveEntityPoint(final EntityPoint p)
	{
		_activeEntityPoint = p;
		notifyChange();
	}

	public boolean isActiveEntityPoint(final SnapPoint p)
	{
		return _activeEntityPoint == p;
	}

	public boolean belongsToActiveSnapPoint(final Entity e)
	{
		if (_activeSnapPoint instanceof EntityPoint)
		{
			return ((EntityPoint) _activeSnapPoint).getFormId().equals(e.getId());
		}

		return _activeSnapPoint instanceof IntersectionSnapPoint && (((IntersectionSnapPoint) _activeSnapPoint)
				.getFormIdA().equals(
				e.getId()) || ((IntersectionSnapPoint) _activeSnapPoint).getFormIdB().equals(e.getId()));
	}

	public void setActiveCropPoint(final CropPoint p)
	{
		_activeCropPoint = p;
		notifyChange();
	}

	public boolean isActiveCropPoint(final CropPoint p)
	{
		return _activeCropPoint == p;
	}

	public CropPoint getActiveCropPoint()
	{
		return _activeCropPoint;
	}

	public void addListener(final ToolStateListener l)
	{
		_listeners.add(l);
	}

	public void removeListener(final ToolStateListener l)
	{
		_listeners.remove(l);
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(final String description)
	{
		_description = description;
		notifyChange();
	}

	public void notifyChange()
	{
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onToolStateChange(this);
		}
	}

	public List<SnapPoint> getSnapPoints()
	{
		return _visibleSnapPoints;
	}

	public List<EntityPoint> getEntityPoints()
	{
		return _entityPoints;
	}

	public void setActiveHandle(final Handle handle)
	{
		if (handle != _activeHandle)
		{
			_activeHandle = handle;
			notifyChange();
		}
	}

	public boolean isActiveHandle(final Handle handle)
	{
		return _activeHandle == handle;
	}

	public void setEntityPoints(final Collection<EntityPoint> points)
	{
		_entityPoints.clear();
		_entityPoints.addAll(points);
		notifyChange();
	}

	public void clearEntityPoints()
	{
		_entityPoints.clear();
		notifyChange();
	}

	public void setPivot(final Vec2 pivot)
	{
		_pivot = pivot;
		notifyChange();
	}

	public Vec2 getPivot()
	{
		return _pivot;
	}

	public void setHandles(final Collection<Handle> handles)
	{
		_handles.clear();
		_handles.addAll(handles);
		notifyChange();
	}

	public List<Handle> getHandles()
	{
		return _handles;
	}

	public SnapPoint getActiveSnapPoint()
	{
		return _activeSnapPoint;
	}

	public Handle getActiveHandle()
	{
		return _activeHandle;
	}

	public EntityPoint getActiveEntityPoint()
	{
		return _activeEntityPoint;
	}


}
