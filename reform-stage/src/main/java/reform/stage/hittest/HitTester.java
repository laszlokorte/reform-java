package reform.stage.hittest;

import reform.math.Vec2;
import reform.stage.Stage;
import reform.stage.elements.*;
import reform.stage.elements.entities.PaperEntity;
import reform.stage.elements.outline.IntersectionSnapPoint;
import reform.stage.elements.outline.IntersectionSnapPointPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HitTester
{

	static private final double _snapRadius = 8;
	private final Stage _stage;
	private final Adapter _adapter;
	private final ArrayList<SnapPoint> _resultSnapPoint = new ArrayList<>();
	private final ArrayList<Entity> _resultEntity = new ArrayList<>();
	private final ArrayList<Handle> _resultHandle = new ArrayList<>();
	final ArrayList<CropPoint> _resultCropPoint = new ArrayList<>();
	final ArrayList<EntityPoint> _resultEntityPoint = new ArrayList<>();
	public HitTester(final Stage stage, final Adapter adapter)
	{
		_stage = stage;
		_adapter = adapter;
	}

	public ArrayList<SnapPoint> getSnapPointsNear(final Vec2 pos, final int max, final
	EntityFilter filter)
	{
		_resultSnapPoint.clear();
		int i = 0;

		outer:
		{
			final List<Entity> entities = _stage.getEntities();
			for (int h = 0, j = entities.size(); h < j; h++)
			{
				final Entity e = entities.get(h);
				if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
				{
					continue;
				}
				if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
				{
					continue;
				}

				final List<EntityPoint> entityPoints = e.getSnapPoints();
				for (int k = 0, l = entityPoints.size(); k < l; k++)
				{
					final EntityPoint p = entityPoints.get(k);
					if (p.isInSnapRadius(pos.x, pos.y))
					{
						_resultSnapPoint.add(p);
						if (++i >= max)
						{
							break outer;
						}
					}
				}
			}

			final List<IntersectionSnapPoint> intersections = _stage
					.getIntersectionPoints();
			for (int h = 0, j = intersections.size(); h < j; h++)
			{
				final IntersectionSnapPoint p = intersections.get(h);
				if (p.isInSnapRadius(pos.x, pos.y))
				{
					if (filter == EntityFilter.ExcludeSelected && _adapter
							.belongsToSelected(p))
					{
						continue;
					}
					if (filter == EntityFilter.OnlySelected && !_adapter
							.belongsToSelected(p))
					{
						continue;
					}

					_resultSnapPoint.add(IntersectionSnapPointPool.copyIfNeeded(p));
					if (++i >= max)
					{
						break outer;
					}
				}
			}
		}

		return _resultSnapPoint;
	}

	public ArrayList<SnapPoint> getSnapPointsNearGlomp(final Vec2 pos, final int max,
	                                                   final EntityFilter filter)
	{
		_resultSnapPoint.clear();
		final List<Entity> entities = _stage.getEntities();
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
			{
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

	public Collection<SnapPoint> getAllSnapPoints(final EntityFilter filter)
	{
		_resultSnapPoint.clear();
		final List<Entity> entities = _stage.getEntities();
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
			{
				continue;
			}
			final List<EntityPoint> snapPoints = e.getSnapPoints();
			for (int k = 0, l = snapPoints.size(); k < l; k++)
			{
				final EntityPoint p = snapPoints.get(k);
				_resultSnapPoint.add(p);
			}
		}

		final List<IntersectionSnapPoint> intersections = _stage.getIntersectionPoints();
		for (int h = 0, j = intersections.size(); h < j; h++)
		{
			final IntersectionSnapPoint s = intersections.get(h);
			if (_adapter.belongsToSelected(s) && filter == EntityFilter.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.belongsToSelected(s) && filter == EntityFilter.OnlySelected)
			{
				continue;
			}
			_resultSnapPoint.add(s);
		}

		return _resultSnapPoint;
	}

	public ArrayList<Entity> getEntityNear(final Vec2 position, final int max)
	{
		_resultEntity.clear();
		int i = 0;
		final List<Entity> entities = _stage.getEntities();
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (e instanceof PaperEntity)
			{
				continue;
			}
			if (i >= max)
			{
				break;
			}
			else if (e.contains(position))
			{
				_resultEntity.add(e);
				i++;
			}
		}

		return _resultEntity;
	}

	public ArrayList<Handle> getHandleNear(final Vec2 pos, final int max, final
	EntityFilter filter, final HandleFilter handleFilter)
	{
		_resultHandle.clear();
		int i = 0;
		final List<Entity> entities = _stage.getEntities();
		outer:
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
			{
				continue;
			}

			final List<Handle> handles = e.getHandles();
			for (int k = 0, l = handles.size(); k < l; k++)
			{
				final Handle handle = handles.get(k);
				if (handleFilter == HandleFilter.Pivot && !handle.hasPivot())
				{
					continue;
				}
				if (handle.isInGrabRadius(pos.x, pos.y))
				{
					_resultHandle.add(handle);
					if (++i >= max)
					{
						break outer;
					}
				}
			}
		}
		return _resultHandle;
	}

	public ArrayList<CropPoint> getCropPointsNear(final Vec2 pos, final int max)
	{
		_resultCropPoint.clear();
		int i = 0;
		outer:
		{
			final List<CropPoint> cropPoints = _stage.getCropPoints();
			for (int h = 0, j = cropPoints.size(); h < j; h++)
			{
				final CropPoint c = cropPoints.get(h);
				if (c.isInGrabRadius(pos.x, pos.y))
				{
					_resultCropPoint.add(c);
					if (++i >= max)
					{
						break outer;
					}

				}
			}
		}

		return _resultCropPoint;
	}

	public ArrayList<EntityPoint> getEntityPointsNear(final Vec2 pos, final int max,
	                                                  final EntityFilter filter)
	{
		_resultEntityPoint.clear();
		int i = 0;
		outer:
		{
			final List<Entity> entities = _stage.getEntities();
			for (int h = 0, j = entities.size(); h < j; h++)
			{
				final Entity e = entities.get(h);
				if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
				{
					continue;
				}
				if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
				{
					continue;
				}

				final List<EntityPoint> entityPoints = e.getSnapPoints();
				for (int k = 0, l = entityPoints.size(); k < l; k++)
				{
					final EntityPoint p = entityPoints.get(k);
					if (p.isInGrabRadius(pos.x, pos.y))
					{
						_resultEntityPoint.add(p);
						if (++i >= max)
						{
							break outer;
						}
					}
				}
			}
		}

		return _resultEntityPoint;
	}

	public ArrayList<EntityPoint> getAllEntityPoints(final EntityFilter filter)
	{
		_resultEntityPoint.clear();
		final List<Entity> entities = _stage.getEntities();
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (_adapter.isSelected(e) && filter == EntityFilter.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.isSelected(e) && filter == EntityFilter.OnlySelected)
			{
				continue;
			}

			final List<EntityPoint> snapPoints = e.getSnapPoints();
			for (int k = 0, l = snapPoints.size(); k < l; k++)
			{
				final EntityPoint p = snapPoints.get(k);
				_resultEntityPoint.add(p);
			}

		}

		return _resultEntityPoint;
	}

	public ArrayList<Handle> getAllHandles(final EntityFilter Filter, final HandleFilter
			handleFilter)
	{
		_resultHandle.clear();
		final List<Entity> entities = _stage.getEntities();
		for (int h = 0, j = entities.size(); h < j; h++)
		{
			final Entity e = entities.get(h);
			if (_adapter.isSelected(
					e) && Filter == reform.stage.hittest.HitTester.EntityFilter
					.ExcludeSelected)
			{
				continue;
			}
			if (!_adapter.isSelected(
					e) && Filter == reform.stage.hittest.HitTester.EntityFilter
					.OnlySelected)
			{
				continue;
			}

			final List<Handle> handles = e.getHandles();
			for (int k = 0, l = handles.size(); k < l; k++)
			{
				final Handle handle = handles.get(k);
				if (handleFilter == HandleFilter.Pivot && !handle.hasPivot())
				{
					continue;
				}
				_resultHandle.add(handle);
			}
		}
		return _resultHandle;
	}

	public enum EntityFilter
	{
		OnlySelected, ExcludeSelected, Any
	}

	public enum HandleFilter
	{
		Pivot, Any
	}

	public interface Adapter
	{
		boolean isSelected(Entity entity);

		boolean belongsToSelected(SnapPoint snapPoint);
	}
}
