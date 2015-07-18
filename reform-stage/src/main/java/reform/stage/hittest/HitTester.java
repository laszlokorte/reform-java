package reform.stage.hittest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import reform.math.Vec2;
import reform.math.Vector;
import reform.stage.Stage;
import reform.stage.elements.CropPoint;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.elements.entities.PaperEntity;
import reform.stage.elements.outline.IntersectionSnapPoint;

public class HitTester {

	public static enum EntityFilter {
		OnlySelected, ExcludeSelected, Any
	}

	public static enum HandleFilter {
		Pivot, Any
	}

	public static interface Adapter {
		public boolean isSelected(Entity entity);

		public boolean belongsToSelected(SnapPoint snapPoint);
	}

	private final Stage _stage;
	private final Adapter _adpater;

	private final double _snapRadius = 8;

	private final ArrayList<SnapPoint> _resultSnapPoint = new ArrayList<>();
	private final ArrayList<Entity> _resultEntitiy = new ArrayList<>();
	private final ArrayList<Handle> _resultHandle = new ArrayList<>();
	final ArrayList<CropPoint> _resultCropPoint = new ArrayList<>();
	final ArrayList<EntityPoint> _resultEntitiyPoint = new ArrayList<>();

	public HitTester(final Stage stage, final Adapter adpter) {
		_stage = stage;
		_adpater = adpter;
	}

	public ArrayList<SnapPoint> getSnapPointsNear(final Vec2 pos, final int max,
			final EntityFilter filter) {
		_resultSnapPoint.clear();
		int i = 0;

		outer: {
			final List<Entity> entities = _stage.getEnties();
			for (int h = 0, j = entities.size(); h < j; h++) {
				final Entity e = entities.get(h);
				if (_adpater.isSelected(e)
						&& filter == EntityFilter.ExcludeSelected) {
					continue;
				}
				if (!_adpater.isSelected(e)
						&& filter == EntityFilter.OnlySelected) {
					continue;
				}

				final List<EntityPoint> entityPoints = e.getSnapPoints();
				for (int k = 0, l = entityPoints.size(); k < l; k++) {
					final EntityPoint p = entityPoints.get(k);
					if (Vector.distance(pos.x, pos.y, p.getX(),
							p.getY()) <= _snapRadius) {
						_resultSnapPoint.add(p);
						if (++i >= max) {
							break outer;
						}
					}
				}
			}

			final List<IntersectionSnapPoint> intersections = _stage
					.getIntersectionPoints();
			for (int h = 0, j = intersections.size(); h < j; h++) {
				final IntersectionSnapPoint p = intersections.get(h);
				if (Vector.distance(pos.x, pos.y, p.getX(),
						p.getY()) <= _snapRadius) {
					if (filter == EntityFilter.ExcludeSelected
							&& _adpater.belongsToSelected(p)) {
						continue;
					}
					if (filter == EntityFilter.OnlySelected
							&& !_adpater.belongsToSelected(p)) {
						continue;
					}

					_resultSnapPoint.add(p);
					if (++i >= max) {
						break outer;
					}
				}
			}
		}

		return _resultSnapPoint;
	}

	public ArrayList<SnapPoint> getSnapPointsNearGlomp(final Vec2 pos,
			final int max, final EntityFilter filter) {
		_resultSnapPoint.clear();
		final List<Entity> entities = _stage.getEnties();
		for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (_adpater.isSelected(e)
					&& filter == EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.isSelected(e)
					&& filter == EntityFilter.OnlySelected) {
				continue;
			}

			// TODO: Outline snapping
			// final Outline outline = f.getOutline();
			// final double t = outline.getWeight(pos.x, pos.y);
			// if (t >= 0 && t <= 1) {
			// result.add(new OutlinePoint(f, t));
			// if (++i >= max) {
			// break outer;
			// }
			// }
		}

		return _resultSnapPoint;
	}

	public Collection<SnapPoint> getAllSnapPoints(final EntityFilter filter) {
		_resultSnapPoint.clear();
		final List<Entity> entities = _stage.getEnties();
		for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (_adpater.isSelected(e)
					&& filter == EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.isSelected(e)
					&& filter == EntityFilter.OnlySelected) {
				continue;
			}
			final List<EntityPoint> snapPoints = e.getSnapPoints();
			for (int k = 0, l = snapPoints.size(); k < l; k++) {
				final EntityPoint p = snapPoints.get(k);
				_resultSnapPoint.add(p);
			}
		}

		final List<IntersectionSnapPoint> intersections = _stage
				.getIntersectionPoints();
		for (int h = 0, j = intersections.size(); h < j; h++) {
			final IntersectionSnapPoint s = intersections.get(h);
			if (_adpater.belongsToSelected(s)
					&& filter == EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.belongsToSelected(s)
					&& filter == EntityFilter.OnlySelected) {
				continue;
			}
			_resultSnapPoint.add(s);
		}

		return _resultSnapPoint;
	}

	public ArrayList<Entity> getEntityNear(final Vec2 position, final int max) {
		_resultEntitiy.clear();
		int i = 0;
		final List<Entity> entities = _stage.getEnties();
		for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (e instanceof PaperEntity) {
				continue;
			}
			if (i >= max) {
				break;
			} else if (e.contains(position)) {
				_resultEntitiy.add(e);
				i++;
			}
		}

		return _resultEntitiy;
	}

	public ArrayList<Handle> getHandleNear(final Vec2 pos, final int max,
			final EntityFilter filter, final HandleFilter handleFilter) {
		_resultHandle.clear();
		int i = 0;
		final List<Entity> entities = _stage.getEnties();
		outer: for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (_adpater.isSelected(e)
					&& filter == EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.isSelected(e)
					&& filter == EntityFilter.OnlySelected) {
				continue;
			}

			final List<Handle> handles = e.getHandles();
			for (int k = 0, l = handles.size(); k < l; k++) {
				final Handle handle = handles.get(k);
				if (handleFilter == HandleFilter.Pivot && !handle.hasPivot()) {
					continue;
				}
				if (Vector.distance(pos.x, pos.y, handle.getX(),
						handle.getY()) <= _snapRadius) {
					_resultHandle.add(handle);
					if (++i >= max) {
						break outer;
					}
				}
			}
		}
		return _resultHandle;
	}

	public ArrayList<CropPoint> getCropPointsNear(final Vec2 pos,
			final int max) {
		_resultCropPoint.clear();
		int i = 0;
		outer: {
			final List<CropPoint> cropPoints = _stage.getCropPoints();
			for (int h = 0, j = cropPoints.size(); h < j; h++) {
				final CropPoint c = cropPoints.get(h);
				if (Vector.distance(pos.x, pos.y, c.getX(),
						c.getY()) <= _snapRadius) {
					_resultCropPoint.add(c);
					if (++i >= max) {
						break outer;
					}

				}
			}
		}

		return _resultCropPoint;
	}

	public ArrayList<EntityPoint> getEntityPointsNear(final Vec2 pos,
			final int max, final EntityFilter filter) {
		_resultEntitiyPoint.clear();
		int i = 0;
		outer: {
			final List<Entity> entities = _stage.getEnties();
			for (int h = 0, j = entities.size(); h < j; h++) {
				final Entity e = entities.get(h);
				if (_adpater.isSelected(e)
						&& filter == EntityFilter.ExcludeSelected) {
					continue;
				}
				if (!_adpater.isSelected(e)
						&& filter == EntityFilter.OnlySelected) {
					continue;
				}

				final List<EntityPoint> entityPoints = e.getSnapPoints();
				for (int k = 0, l = entityPoints.size(); k < l; k++) {
					final EntityPoint p = entityPoints.get(k);
					if (Vector.distance(pos.x, pos.y, p.getX(),
							p.getY()) <= _snapRadius) {
						_resultEntitiyPoint.add(p);
						if (++i >= max) {
							break outer;
						}
					}
				}
			}
		}

		return _resultEntitiyPoint;
	}

	public ArrayList<EntityPoint> getAllEntityPoints(
			final EntityFilter filter) {
		_resultEntitiyPoint.clear();
		final List<Entity> entities = _stage.getEnties();
		for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (_adpater.isSelected(e)
					&& filter == EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.isSelected(e)
					&& filter == EntityFilter.OnlySelected) {
				continue;
			}

			final List<EntityPoint> snapPoints = e.getSnapPoints();
			for (int k = 0, l = snapPoints.size(); k < l; k++) {
				final EntityPoint p = snapPoints.get(k);
				_resultEntitiyPoint.add(p);
			}

		}

		return _resultEntitiyPoint;
	}

	public ArrayList<Handle> getAllHandles(final EntityFilter Filter,
			final HandleFilter handleFilter) {
		_resultHandle.clear();
		final List<Entity> entities = _stage.getEnties();
		for (int h = 0, j = entities.size(); h < j; h++) {
			final Entity e = entities.get(h);
			if (_adpater.isSelected(e)
					&& Filter == reform.stage.hittest.HitTester.EntityFilter.ExcludeSelected) {
				continue;
			}
			if (!_adpater.isSelected(e)
					&& Filter == reform.stage.hittest.HitTester.EntityFilter.OnlySelected) {
				continue;
			}

			final List<Handle> handles = e.getHandles();
			for (int k = 0, l = handles.size(); k < l; k++) {
				final Handle handle = handles.get(k);
				if (handleFilter == HandleFilter.Pivot && !handle.hasPivot()) {
					continue;
				}
				_resultHandle.add(handle);
			}
		}
		return _resultHandle;
	}
}