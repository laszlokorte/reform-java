package reform.stage.tooling.cursor;

import java.util.ArrayList;

import reform.math.Vec2;
import reform.stage.elements.CropPoint;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.SnapPoint;
import reform.stage.hittest.HitTester;
import reform.stage.hittest.HitTester.EntityFilter;
import reform.stage.hittest.HitTester.HandleFilter;

public class Cursor {

	private final HitTester _hitTester;
	private final Vec2 _position = new Vec2(-9999, -9999);

	private int _snapCycle = 0;
	private final ArrayList<SnapPoint> _snapPointCache = new ArrayList<>();
	private SnapPoint _prevSnap;

	private int _entityPointCycle = 0;
	private final ArrayList<EntityPoint> _entityPointCache = new ArrayList<>();

    private int _snapGlompCycle = 0;
	private final ArrayList<SnapPoint> _snapPointGlompCache = new ArrayList<>();
	private SnapPoint _prevSnapGlomp;

	private int _entityCycle = 0;
	private final ArrayList<Entity> _entityCache = new ArrayList<>();

    private int _handleCycle = 0;
	private final ArrayList<Handle> _handleCache = new ArrayList<>();

    private int _cropCycle = 0;
	private final ArrayList<CropPoint> _cropPointCache = new ArrayList<>();

    public Cursor(final HitTester hitTester) {
		_hitTester = hitTester;
	}

	public void setPosition(final double x, final double y) {
		_position.set(x, y);
		_snapPointCache.clear();
		_snapPointGlompCache.clear();
		_entityCache.clear();
		_handleCache.clear();
		_cropPointCache.clear();
		_entityPointCache.clear();

	}

	public SnapPoint getSnapPoint(final HitTester.EntityFilter filter) {
		if (_snapPointCache.isEmpty() || _snapCycle >= _snapPointCache.size()) {
			_snapPointCache.clear();
			_snapPointCache.addAll(_hitTester.getSnapPointsNear(_position,
                    _snapCycle + 1, filter));
		}
		if (!_snapPointCache.isEmpty()) {
			_prevSnap = _snapPointCache
					.get(_snapCycle % _snapPointCache.size());
		} else {
			_prevSnap = null;
		}
		return _prevSnap;
	}

	public EntityPoint getEntityPoint(final HitTester.EntityFilter filter) {
		if (_entityPointCache.isEmpty()
				|| _entityPointCycle >= _entityPointCache.size()) {
			_entityPointCache.clear();
			_entityPointCache.addAll(_hitTester.getEntityPointsNear(_position,
                    _entityPointCycle + 1, filter));
		}
        EntityPoint prevEntityPoint;
        if (!_entityPointCache.isEmpty()) {
			prevEntityPoint = _entityPointCache
					.get(_entityPointCycle % _entityPointCache.size());
		} else {
			prevEntityPoint = null;
		}
		return prevEntityPoint;
	}

	public SnapPoint getSnapPointGlomp(final HitTester.EntityFilter filter) {
		if (_snapPointGlompCache.isEmpty()
				|| _snapGlompCycle >= _snapPointGlompCache.size()) {
			_snapPointGlompCache.clear();
			_snapPointGlompCache.addAll(_hitTester.getSnapPointsNearGlomp(
					_position, _snapGlompCycle + 1, filter));

		}
		if (!_snapPointGlompCache.isEmpty()) {
			_prevSnapGlomp = _snapPointGlompCache
					.get(_snapGlompCycle % _snapPointGlompCache.size());
		} else {
			_prevSnapGlomp = null;
		}

		return _prevSnapGlomp;
	}

	public CropPoint getCropPoint() {
		if (_cropPointCache.isEmpty() || _cropCycle >= _cropPointCache.size()) {
			_cropPointCache.clear();
			_cropPointCache.addAll(
					_hitTester.getCropPointsNear(_position, _cropCycle + 1));
		}
        CropPoint prevCropPoint;
        if (!_cropPointCache.isEmpty()) {
			prevCropPoint = _cropPointCache
					.get(_cropCycle % _cropPointCache.size());
		} else {
			prevCropPoint = null;
		}
		return prevCropPoint;
	}

	public void resetCycle() {
		_snapCycle = 0;
		_snapGlompCycle = 0;
		_cropCycle = 0;
		_entityCycle = 0;
		_handleCycle = 0;
		_entityPointCycle = 0;
		_snapPointCache.clear();
		_snapPointGlompCache.clear();
		_entityCache.clear();
		_handleCache.clear();
		_cropPointCache.clear();
		_entityPointCache.clear();
	}

	public void cycleNextHandle() {
		_handleCycle++;

	}

	public void cycleNextSnap() {
		_snapCycle++;

	}

	public void cycleNextSnapGlomp() {
		_snapGlompCycle++;

	}

	public void cycleNextEntity() {
		_entityCycle++;

	}

	public void cycleNextCropPoint() {
		_cropCycle++;
	}

	public void cycleNextEntityPoint() {
		_entityPointCycle++;
	}

	public Vec2 getPosition() {
		return _position;
	}

	public Entity getEntity() {
		if (_entityCache.isEmpty() || _entityCycle >= _entityCache.size()) {
			_entityCache.clear();
			_entityCache.addAll(
					_hitTester.getEntityNear(_position, _entityCycle + 1));
		}
        Entity prevEntity;
        if (!_entityCache.isEmpty()) {
			prevEntity = _entityCache.get(_entityCycle % _entityCache.size());
		} else {
			prevEntity = null;
		}
		return prevEntity;
	}

	public Handle getHandle(final EntityFilter filter,
			final HandleFilter handleFilter) {
		if (_handleCache.isEmpty() || _handleCycle >= _handleCache.size()) {
			_handleCache.clear();
			_handleCache.addAll(_hitTester.getHandleNear(_position,
					_handleCycle + 1, filter, handleFilter));
		}
        Handle prevHandle;
        if (!_handleCache.isEmpty()) {
			prevHandle = _handleCache.get(_handleCycle % _handleCache.size());
		} else {
			prevHandle = null;
		}
		return prevHandle;
	}

	public double getX() {
		return _position.x;
	}

	public double getY() {
		return _position.y;
	}

}
