package reform.stage;

import java.awt.geom.GeneralPath;

import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.PoolFactory;
import reform.core.pool.SimplePool;
import reform.core.runtime.Evaluatable;
import reform.core.runtime.ProjectRuntime;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.stage.elements.Entity;
import reform.stage.elements.factory.EntityCache;
import reform.stage.elements.factory.EntityFactory;

public class StageCollector implements ProjectRuntime.Listener {
	public static interface Adapter {
		public boolean isInFocus(Evaluatable instruction);
	}

	private final Stage _stage;
	private final Adapter _adpter;

	private final Pool<GeneralPath.Double> _pathPool = new SimplePool<>(
			new PoolFactory<GeneralPath.Double>() {
				@Override
				public GeneralPath.Double create() {
					return new GeneralPath.Double();
				};
			});

	private final StageBuffer _buffer = new StageBuffer();
	private final EntityCache _entityCache = new EntityCache();
	private final EntityFactory _entityFactory = new EntityFactory();

	private boolean _collected;

	public StageCollector(final Stage stage, final Adapter adapter) {
		_stage = stage;
		_adpter = adapter;
	}

	@Override
	public void onBeginEvaluation(final ProjectRuntime runtime) {
		_entityCache.coolDown();
		_collected = false;
		_buffer.clear();
		_buffer.setSize(runtime.getSize());
	}

	@Override
	public void onFinishEvaluation(final ProjectRuntime runtime) {
		_entityCache.cleanUp();
		_buffer.flush(_stage);
		_pathPool.release();
	}

	@Override
	public void onEvalInstruction(final ProjectRuntime runtime,
			final Evaluatable instruction) {
		if (_adpter.isInFocus(instruction)) {
			if (_collected) {
				return;
			} else {
				_collected = true;
			}
			final FastIterable<Identifier<? extends Form>> it = runtime
					.getStackIterator();
			for (int i = 0; i < it.size(); i++) {
				final Identifier<? extends Form> id = it.get(i);
				final Form form = runtime.get(id);

				final Entity entity = _entityCache.fetch(form, _entityFactory);
				entity.updateForRuntime(runtime);
				_buffer.addEntity(entity);

				if (form.getType() == DrawingType.Draw) {
					final GeneralPath.Double shape = _pathPool.take();
					shape.reset();
					runtime.get(id).appendToPathForRuntime(runtime, shape);
					_buffer.addShape(shape, id);
				}
			}

		}
	}

	@Override
	public void onPopScope(final ProjectRuntime runtime,
			final FastIterable<Identifier<? extends Form>> poppedIds) {
		for (int i = 0, j = poppedIds.size(); i < j; i++) {
			final Identifier<? extends Form> id = poppedIds.get(i);
			final Form form = runtime.get(id);

			if (form.getType() == DrawingType.Draw) {
				final GeneralPath.Double shape = _pathPool.take();
				shape.reset();
				form.appendToPathForRuntime(runtime, shape);
				if (!_collected) {
					_buffer.addShape(shape, id);
				}

				_buffer.addFinalShape(shape, id);
			}
		}
	}

	@Override
	public void onError(final ProjectRuntime runtime,
			final Evaluatable instruction, final Error error) {
		// TODO Auto-generated method stub

	}

}