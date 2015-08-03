package reform.stage;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.PictureForm;
import reform.core.graphics.ColoredShape;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.runtime.*;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.RuntimeError;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.stage.elements.Entity;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.factory.ControlCache;
import reform.stage.elements.factory.ControlFactory;
import reform.stage.elements.factory.EntityCache;
import reform.stage.elements.factory.EntityFactory;

import java.awt.*;
import java.util.ArrayList;

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

	private final int[] _sizes = new int[Runtime.MAX_DEPTH * 2];
	private final ColoredShape[] _subShapeStack = new ColoredShape[Runtime.MAX_DEPTH];
	private final ArrayList<ColoredShape> _subshapes = new ArrayList<>(5);

	public StageCollector(final Stage stage, final Adapter adapter, final Analyzer
			analyzer)
	{
		_stage = stage;
		_analyzer = analyzer;
		_adapter = adapter;

		for(int i=0,j=_subShapeStack.length;i<j;i++) {
			_subShapeStack[i] = new ColoredShape();
		}
	}

	@Override
	public void onBeginEvaluation(final Runtime runtime)
	{
		if(runtime.getDepth() > 0) {
			int d = runtime.getDepth() - 1;
			Vec2i size = runtime.getSize();
			_subShapeStack[d].reset();
			_sizes[2*d] = size.x;
			_sizes[2*d+1] = size.y;

			return;
		}

		_subshapes.clear();
		_entityCache.coolDown();
		_collected = false;
		_errorOnCurrent = false;
		_buffer.clear();
		_buffer.setSize(runtime.getSize());
		_pathPool.clean(ColoredShape::reset);
	}

	@Override
	public void onFinishEvaluation(final Runtime runtime)
	{
		if(runtime.getDepth() > 0) {
			int d = runtime.getDepth()-1;
			ColoredShape shape = _pathPool.take();
			shape.reset();
			shape.addSubShapesFrom(_subShapeStack[d]);
			_subshapes.add(shape);
			return;
		}
		_entityCache.cleanUp();
		_buffer.flush(_stage);
		_pathPool.release();
	}

	@Override
	public void onEvalInstruction(final Runtime runtime, final Evaluable
			instruction)
	{

		if(runtime.getDepth() > 0) {
			return;
		}

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
			int pics = 0;
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
					if(form instanceof PictureForm && !_subshapes.isEmpty()) {
						ColoredShape subShape = _subshapes.get(_subshapes.size() - ++pics);
						shape.addSubShapesFrom(subShape);
					}
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

		if(runtime.getDepth() > 0) {
			int d = runtime.getDepth() - 1;

			for (int i = 0, j = poppedIds.size(); i < j; i++)
			{
				final Identifier<? extends Form> id = poppedIds.get(i);
				final Form form = runtime.get(id);

				if (form.getType() == DrawingType.Draw)
				{
					final ColoredShape shape = _pathPool.take();
					shape.reset();
					form.writeColoredShapeForRuntime(runtime, shape);
					if(form instanceof PictureForm && !_subshapes.isEmpty()) {
						ColoredShape subShape = _subshapes.remove(_subshapes.size() - 1);
						shape.addSubShapesFrom(subShape);
					}

					_subShapeStack[d].addSubShape(shape);
				}
			}

			return;
		}

		for (int i = 0, j = poppedIds.size(); i < j; i++)
		{
			final Identifier<? extends Form> id = poppedIds.get(i);
			final Form form = runtime.get(id);

			if (form.getType() == DrawingType.Draw)
			{
				final ColoredShape shape = _pathPool.take();
				shape.reset();
				form.writeColoredShapeForRuntime(runtime, shape);
				if(form instanceof PictureForm) {
					ColoredShape subShape = _subshapes.remove(_subshapes.size() - 1);

					shape.addSubShapesFrom(subShape);
				}

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

		if(runtime.getDepth() > 0) {
			return;
		}

		if (_adapter.isInFocus(instruction) && !_collected)
		{
			_errorOnCurrent = true;
		}
	}

	@Override
	public void onSubCallBegin(final Runtime runtime)
	{

	}

	@Override
	public void onSubCallEnd(final Runtime runtime)
	{

	}

	public interface Adapter
	{
		boolean isInFocus(Evaluable instruction);
	}

}
