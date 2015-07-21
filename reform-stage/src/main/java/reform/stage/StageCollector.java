package reform.stage;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.runtime.Evaluable;
import reform.core.runtime.ProjectRuntime;
import reform.core.runtime.errors.InvalidDestinationError;
import reform.core.runtime.errors.RuntimeError;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.errors.DestinationMarker;
import reform.stage.elements.factory.EntityCache;
import reform.stage.elements.factory.EntityFactory;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

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
			if(error instanceof InvalidDestinationError) {
				InvalidDestinationError e = (InvalidDestinationError) error;
				_buffer.setErrorMarker(new DestinationMarker(e.getDestination(), new Vec2(runtime.getSize().x / 3, runtime.getSize().y / 3)));
			}
		}
	}

}
