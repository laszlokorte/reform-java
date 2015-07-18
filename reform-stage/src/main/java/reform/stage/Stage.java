package reform.stage;

import java.awt.Shape;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import reform.core.forms.Form;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.stage.elements.CropPoint;
import reform.stage.elements.Entity;
import reform.stage.elements.outline.IntersectionSnapPoint;

public class Stage {
	public interface Listener {
		void onStageComplete(Stage stage);
	}

	private final Vec2i _size = new Vec2i();
	private final CopyOnWriteArrayList<Listener> _listeners = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<Entity> _entities = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<Shape> _currentShapes = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<Shape> _finalShapes = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<CropPoint> _cropPoints = new CopyOnWriteArrayList<>();
	private final CopyOnWriteArrayList<IntersectionSnapPoint> _intersectionPoints = new CopyOnWriteArrayList<>();

	private final HashMap<Shape, Identifier<? extends Form>> _shapeMap = new HashMap<>();
	private final HashMap<Identifier<? extends Form>, Entity> _entityMap = new HashMap<>();

	public Stage() {

		_cropPoints.add(new CropPoint(-1, 0));
		_cropPoints.add(new CropPoint(1, 0));
		_cropPoints.add(new CropPoint(0, 1));
		_cropPoints.add(new CropPoint(0, -1));

		_cropPoints.add(new CropPoint(-1, -1));
		_cropPoints.add(new CropPoint(1, -1));
		_cropPoints.add(new CropPoint(1, 1));
		_cropPoints.add(new CropPoint(-1, 1));

	}

	public void addListener(final Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener) {
		_listeners.remove(listener);
	}

	public List<Entity> getEnties() {
		return _entities;
	}

	public List<IntersectionSnapPoint> getIntersectionPoints() {
		return _intersectionPoints;
	}

	public Vec2i getSize() {
		return _size;
	}

	public List<Shape> getCurrentShapes() {
		return _currentShapes;
	}

	public List<Shape> getFinalShapes() {
		return _finalShapes;
	}

	public Identifier<? extends Form> getIdFor(final Shape s) {
		return _shapeMap.get(s);
	}

	void wipe() {
		_finalShapes.clear();
		_currentShapes.clear();
		_entities.clear();
		_intersectionPoints.clear();
		_entityMap.clear();
		_shapeMap.clear();
		_size.set(0, 0);
	}

	void setSize(final Vec2i size) {
		_size.set(size);

		for (int i = 0; i < _cropPoints.size(); i++) {
			_cropPoints.get(i).updatePosition(size.x, size.y);
		}
	}

	void complete() {
		Collections.reverse(_entities);
		for (int i = 0; i < _listeners.size(); i++) {
			_listeners.get(i).onStageComplete(this);
		}
	}

	void addShape(final Shape shape,
                  final Identifier<? extends Form> id) {
		_currentShapes.add(shape);
		_shapeMap.put(shape, id);
	}

	void addFinalShape(final Shape shape,
                       final Identifier<? extends Form> id) {
		_finalShapes.add(shape);
		_shapeMap.put(shape, id);
	}

	void addEntity(final Entity entity) {
		_entities.add(entity);
		_entityMap.put(entity.getId(), entity);
	}

	void addIntersectionPoint(
            final IntersectionSnapPoint intersectionPoint) {
		_intersectionPoints.add(intersectionPoint);
	}

	public Entity getEntityForId(final Identifier<? extends Form> formId) {
		return _entityMap.get(formId);
	}

	public List<CropPoint> getCropPoints() {
		return _cropPoints;
	}
}
