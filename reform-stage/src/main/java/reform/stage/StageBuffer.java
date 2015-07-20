package reform.stage;

import reform.core.forms.Form;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vec2i;
import reform.stage.elements.Entity;
import reform.stage.elements.outline.EntityOutline;
import reform.stage.elements.outline.IntersectionSnapPointPool;

import java.awt.*;
import java.util.ArrayList;

class StageBuffer
{

	private final Vec2i _size = new Vec2i();
	private final ArrayList<Entity> _entities = new ArrayList<>();
	private final ArrayList<Shape> _currentShapes = new ArrayList<>();
	private final ArrayList<Shape> _finalShapes = new ArrayList<>();

	private final ArrayList<Identifier<? extends Form>> _currentShapeIds = new ArrayList<>();
	private final ArrayList<Identifier<? extends Form>> _finalShapeIds = new ArrayList<>();

	private final IntersectionSnapPointPool _intersectionSnapPointPool = new IntersectionSnapPointPool();

	public void clear()
	{
		_finalShapes.clear();
		_currentShapes.clear();
		_entities.clear();
		_currentShapeIds.clear();
		_finalShapeIds.clear();
		_size.set(0, 0);
	}

	public void flush(final Stage stage)
	{
		//synchronized (stage) {
		stage.wipe();
		stage.setSize(_size);
		_intersectionSnapPointPool.release();
		for (int i = 0; i < _entities.size(); i++)
		{
			final Entity e = _entities.get(i);
			stage.addEntity(e);

			for (int j = 0; j < i; j++)
			{
				final Entity other = _entities.get(j);
				final Vec2[] intersections = EntityOutline.intersect(e.getOutline(), other.getOutline());
				for (int k = intersections.length - 1; k >= 0; k--)
				{
					if (intersections[k] != null)
					{
						stage.addIntersectionPoint(_intersectionSnapPointPool.create(e, other, k, intersections[k]));
					}
				}
			}
		}
		for (int i = 0; i < _currentShapes.size(); i++)
		{
			final Shape s = _currentShapes.get(i);
			stage.addShape(s, _currentShapeIds.get(i));
		}
		for (int i = 0; i < _finalShapes.size(); i++)
		{
			final Shape s = _finalShapes.get(i);
			stage.addFinalShape(s, _finalShapeIds.get(i));
		}
		stage.complete();
		//}
	}

	public void setSize(final Vec2i size)
	{
		_size.set(size);
	}

	public void addShape(final Shape shape, final Identifier<? extends Form> id)
	{
		_currentShapes.add(shape);
		_currentShapeIds.add(id);
	}

	public void addFinalShape(final Shape shape, final Identifier<? extends Form> id)
	{
		_finalShapes.add(shape);
		_finalShapeIds.add(id);
	}

	public void addEntity(final Entity entity)
	{
		_entities.add(entity);
	}
}
