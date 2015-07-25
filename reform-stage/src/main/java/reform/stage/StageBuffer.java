package reform.stage;

import reform.core.forms.Form;
import reform.core.graphics.ColoredShape;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vec2i;
import reform.stage.elements.Entity;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.outline.EntityOutline;
import reform.stage.elements.outline.IntersectionSnapPointPool;

import java.util.ArrayList;

class StageBuffer
{

	private final Vec2i _size = new Vec2i();
	private final ArrayList<Entity> _entities = new ArrayList<>();
	private final ArrayList<ColoredShape> _currentShapes = new ArrayList<>();
	private final ArrayList<ColoredShape> _finalShapes = new ArrayList<>();

	private final ArrayList<Identifier<? extends Form>> _currentShapeIds = new ArrayList<>();
	private final ArrayList<Identifier<? extends Form>> _finalShapeIds = new ArrayList<>();

	private final IntersectionSnapPointPool _intersectionSnapPointPool = new IntersectionSnapPointPool();
	private InstructionControl _instructionControl = null;


	public void clear()
	{
		_instructionControl = null;
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
		for (int i = 0, j = _entities.size(); i < j; i++)
		{
			final Entity e = _entities.get(i);
			stage.addEntity(e);

			for (int k = 0; k < i; k++)
			{
				final Entity other = _entities.get(k);
				final Vec2[] intersections = EntityOutline.intersect(e.getOutline(), other.getOutline());
				for (int l = intersections.length - 1; l >= 0; l--)
				{
					if (intersections[l] != null)
					{
						stage.addIntersectionPoint(_intersectionSnapPointPool.create(e, other, l, intersections[l]));
					}
				}
			}
		}
		for (int i = 0; i < _currentShapes.size(); i++)
		{
			final ColoredShape s = _currentShapes.get(i);
			stage.addShape(s, _currentShapeIds.get(i));
		}
		for (int i = 0; i < _finalShapes.size(); i++)
		{
			final ColoredShape s = _finalShapes.get(i);
			stage.addFinalShape(s, _finalShapeIds.get(i));
		}
		if (_instructionControl != null)
		{
			stage.setInstructionControl(_instructionControl);
		}
		stage.complete();
		//}
	}

	public void setSize(final Vec2i size)
	{
		_size.set(size);
	}

	public void addShape(final ColoredShape shape, final Identifier<? extends Form> id)
	{
		_currentShapes.add(shape);
		_currentShapeIds.add(id);
	}

	public void addFinalShape(final ColoredShape shape, final Identifier<? extends Form> id)
	{
		_finalShapes.add(shape);
		_finalShapeIds.add(id);
	}

	public void addEntity(final Entity entity)
	{
		_entities.add(entity);
	}

	public void setInstructionControl(final InstructionControl control)
	{
		_instructionControl = control;
	}
}
