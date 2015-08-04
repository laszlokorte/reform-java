package reform.stage;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.runtime.*;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.RuntimeError;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.stage.elements.Entity;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.factory.ControlCache;
import reform.stage.elements.factory.ControlFactory;
import reform.stage.elements.factory.EntityCache;
import reform.stage.elements.factory.EntityFactory;

public class StageCollector implements Runtime.Listener
{
	private final Analyzer _analyzer;
	private final Stage _stage;
	private final Adapter _adapter;
	private final Pool<ColoredShape> _pathPool = new SimplePool<>(ColoredShape::new);
	private final StageBuffer _buffer = new StageBuffer();
	private final EntityCache _entityCache = new EntityCache();
	private final EntityFactory _entityFactory = new EntityFactory();
	private final ControlCache _controlCache = new ControlCache();
	private final ControlFactory _controlFactory = new ControlFactory();
	private boolean _collected;
	private boolean _errorOnCurrent = false;
	public StageCollector(final Stage stage, final Adapter adapter, final Analyzer
			analyzer)
	{
		_stage = stage;
		_analyzer = analyzer;
		_adapter = adapter;
	}

	@Override
	public void onBeginEvaluation(final Runtime runtime)
	{
		_entityCache.coolDown();
		_collected = false;
		_errorOnCurrent = false;
		_buffer.clear();
		_buffer.setSize(runtime.getSize());
	}

	@Override
	public void onFinishEvaluation(final Runtime runtime)
	{
		_entityCache.cleanUp();
		_buffer.flush(_stage);
		_pathPool.release();
	}

	@Override
	public void onEvalInstruction(final Runtime runtime, final Evaluable
			instruction)
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
			final FastIterable<Identifier<? extends Form>> it = runtime
					.getStackIterator();
			for (int i = 0, j = it.size(); i < j; i++)
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
			if (_errorOnCurrent)
			{
				final InstructionControl control = _controlCache.fetch(instruction,
				                                                       _controlFactory);
				control.updateForRuntime(runtime);
				_buffer.setInstructionControl(control);
			}
		}
	}

	@Override
	public void onPopScope(final Runtime runtime, final FastIterable<Identifier<?
			extends Form>> poppedIds)
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
	public void onError(final reform.core.runtime.Runtime runtime, final Evaluable instruction, final
	RuntimeError error)
	{
		if (_adapter.isInFocus(instruction) && !_collected)
		{
			_errorOnCurrent = true;
		}
	}

	public interface Adapter
	{
		boolean isInFocus(Evaluable instruction);
	}

}
