package reform.stage;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.relations.RelativeDistance;
import reform.core.forms.relations.RelativeDynamicSizeDestination;
import reform.core.forms.relations.RelativeFixSizeDestination;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.runtime.Evaluable;
import reform.core.runtime.ProjectRuntime;
import reform.core.runtime.errors.InvalidDestinationError;
import reform.core.runtime.errors.InvalidDistanceError;
import reform.core.runtime.errors.RuntimeError;
import reform.core.runtime.relations.InitialDestination;
import reform.core.runtime.relations.ReferencePoint;
import reform.core.runtime.relations.TranslationDistance;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.errors.DynamicSizeDestinationMarker;
import reform.stage.elements.errors.FixSizeDestinationMarker;
import reform.stage.elements.errors.RelativeDistanceMarker;
import reform.stage.elements.factory.EntityCache;
import reform.stage.elements.factory.EntityFactory;

public class StageCollector implements ProjectRuntime.Listener
{
	private final Analyzer _analyzer;

	public interface Adapter
	{
		boolean isInFocus(Evaluable instruction);
	}

	private final Stage _stage;
	private final Adapter _adapter;

	private final Pool<ColoredShape> _pathPool = new SimplePool<>(ColoredShape::new);

	private final StageBuffer _buffer = new StageBuffer();
	private final EntityCache _entityCache = new EntityCache();
	private final EntityFactory _entityFactory = new EntityFactory();

	private boolean _collected;

	public StageCollector(final Stage stage, final Adapter adapter, final Analyzer analyzer)
	{
		_stage = stage;
		_analyzer = analyzer;
		_adapter = adapter;
	}

	@Override
	public void onBeginEvaluation(final ProjectRuntime runtime)
	{
		_entityCache.coolDown();
		_collected = false;
		_buffer.clear();
		_buffer.setSize(runtime.getSize());
	}

	@Override
	public void onFinishEvaluation(final ProjectRuntime runtime)
	{
		_entityCache.cleanUp();
		_buffer.flush(_stage);
		_pathPool.release();
	}

	@Override
	public void onEvalInstruction(final ProjectRuntime runtime, final Evaluable instruction)
	{
		if (_adapter.isInFocus(instruction))
		{
			if (_collected)
			{
				return;
			}
			else
			{
				_collected = true;
			}
			final FastIterable<Identifier<? extends Form>> it = runtime.getStackIterator();
			for (int i = 0; i < it.size(); i++)
			{
				final Identifier<? extends Form> id = it.get(i);
				final Form form = runtime.get(id);

				final Entity entity = _entityCache.fetch(form, _entityFactory);
				entity.updateForRuntime(runtime, _analyzer);
				_buffer.addEntity(entity);

				if (form.getType() == DrawingType.Draw)
				{
					final ColoredShape shape = _pathPool.take();
					shape.reset();
					runtime.get(id).writeColoredShapeForRuntime(runtime, shape);
					_buffer.addShape(shape, id);
				}
			}

		}
	}

	@Override
	public void onPopScope(final ProjectRuntime runtime, final FastIterable<Identifier<? extends Form>> poppedIds)
	{
		for (int i = 0, j = poppedIds.size(); i < j; i++)
		{
			final Identifier<? extends Form> id = poppedIds.get(i);
			final Form form = runtime.get(id);

			if (form.getType() == DrawingType.Draw)
			{
				final ColoredShape shape = _pathPool.take();
				shape.reset();
				form.writeColoredShapeForRuntime(runtime, shape);
				if (!_collected)
				{
					_buffer.addShape(shape, id);
				}

				_buffer.addFinalShape(shape, id);
			}
		}
	}

	@Override
	public void onError(final ProjectRuntime runtime, final Evaluable instruction, final RuntimeError error)
	{
		if (_adapter.isInFocus(instruction) && !_collected)
		{
			if (error instanceof InvalidDestinationError)
			{
				InvalidDestinationError e = (InvalidDestinationError) error;
				InitialDestination d = e.getDestination();
				if (d instanceof RelativeDynamicSizeDestination)
				{
					RelativeDynamicSizeDestination r = (RelativeDynamicSizeDestination) d;
					ReferencePoint a = r.getReferenceA();
					ReferencePoint b = r.getReferenceB();

					if (!a.isValidFor(runtime))
					{
						_buffer.addErrorMarker(new DynamicSizeDestinationMarker(r, new Vec2(40, 80)));
					}

					if (!b.isValidFor(runtime))
					{
						_buffer.addErrorMarker(new DynamicSizeDestinationMarker(r, new Vec2(90, 30)));
					}

				}
				else if (d instanceof RelativeFixSizeDestination)
				{
					RelativeFixSizeDestination r = (RelativeFixSizeDestination) d;
					ReferencePoint a = r.getReference();

					_buffer.addErrorMarker(new FixSizeDestinationMarker(r, new Vec2(40, 40)));
				}
			}
			else if (error instanceof InvalidDistanceError)
			{
				InvalidDistanceError e = (InvalidDistanceError) error;
				TranslationDistance d = e.getDistance();

				if (d instanceof RelativeDistance)
				{
					RelativeDistance r = (RelativeDistance) d;

					ReferencePoint a = r.getReferenceA();
					ReferencePoint b = r.getReferenceB();

					if (a.isValidFor(runtime))
					{
						if (!b.isValidFor(runtime))
						{
							_buffer.addErrorMarker(new RelativeDistanceMarker(r,
							                                                  new Vec2(a.getXValueForRuntime(runtime),
							                                                           a.getYValueForRuntime(
									                                                           runtime))));
						}
						else
						{
							throw new RuntimeException(
									"Should never get here: RelativeDistance is invalid, but both ref points are valid" +
											".");
						}
					}
					else
					{
						if (!b.isValidFor(runtime))
						{
							_buffer.addErrorMarker(new RelativeDistanceMarker(r, new Vec2(70, 50)));
						}

						_buffer.addErrorMarker(new RelativeDistanceMarker(r, new Vec2(40, 90)));
					}
				}

			}
		}
	}

}
