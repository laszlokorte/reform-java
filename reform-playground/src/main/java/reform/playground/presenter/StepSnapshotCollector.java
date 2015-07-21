package reform.playground.presenter;

import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.NullInstruction;
import reform.core.runtime.Evaluable;
import reform.core.runtime.ProjectRuntime;
import reform.core.runtime.errors.RuntimeError;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class StepSnapshotCollector implements ProjectRuntime.Listener
{

	public interface Listener
	{
		void onCollectionCompleted(StepSnapshotCollector collector);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final Pool<GeneralPath.Double> _pathPool = new SimplePool<>(Path2D.Double::new);

	private final Vec2i _maxSize = new Vec2i();
	private final HashMap<RenderingHints.Key, Object> _renderOptions = new HashMap<>();

	private final Vec2i _currentSize = new Vec2i();
	private final Vec2i _currentScaledSize = new Vec2i(_maxSize);
	private final Map<Evaluable, BufferedImage> _instructionBitmaps = new HashMap<>();
	private final Map<Evaluable, RuntimeError> _errorMap = new HashMap<>();
	private final Set<Evaluable> _recordedInstructions = new HashSet<>();
	private final Set<Evaluable> _failedInstructions = new HashSet<>();
	private final CopyOnWriteArrayList<Shape> _collectedShapes = new CopyOnWriteArrayList<>();

	private final Color _colorShape = new Color(0x555555);
	private final Color _colorActive = new Color(0x23A9E5);
	private final Color _colorGuide = new Color(0x00ffff);

	private final Stroke _stroke = new BasicStroke(5);
	private final Stroke _guideStroke = new BasicStroke(15);

	private boolean _redraw = true;

	public StepSnapshotCollector(final Vec2i maxSize)
	{
		_maxSize.set(maxSize);
		_renderOptions.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		_renderOptions.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	}

	@Override
	public void onEvalInstruction(final ProjectRuntime runtime, final Evaluable evaluable)
	{
		if (evaluable instanceof NullInstruction)
		{
			return;
		}

		if (evaluable instanceof InstructionGroup)
		{
			return;
		}

		if (_failedInstructions.contains(evaluable))
		{
			return;
		}

		if (!_recordedInstructions.add(evaluable) || _failedInstructions.contains(evaluable))
		{
			return;
		}

		if (_instructionBitmaps.containsKey(evaluable) && !_redraw)
		{
			return;
		}

		final BufferedImage bufImg = getBufferedImage(evaluable);
		final int width = bufImg.getWidth();
		final int height = bufImg.getHeight();
		final Vec2i size = runtime.getSize();

		final Instruction instruction = (Instruction) evaluable;

		final Graphics2D g2 = (Graphics2D) bufImg.getGraphics();
		g2.setRenderingHints(_renderOptions);

		g2.clearRect(0, 0, width, height);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		g2.scale(1.0 * width / size.x, 1.0 * height / size.y);

		g2.setColor(_colorShape);
		g2.setStroke(_stroke);
		for (int i = 0, j = _collectedShapes.size(); i < j; i++)
		{
			final Shape shape = _collectedShapes.get(i);
			g2.fill(shape);
			g2.draw(shape);
		}

		final FastIterable<Identifier<? extends Form>> it = runtime.getStackIterator();
		for (int i = 0; i < it.size(); i++)
		{
			final Identifier<? extends Form> id = it.get(i);
			final Form form = runtime.get(id);
			final boolean isCurrent = id.equals(instruction.getTarget());
			final boolean isGuide = form.getType() != DrawingType.Draw;
			if (isGuide && !isCurrent)
			{
				continue;
			}
			final GeneralPath.Double shape = _pathPool.take();
			shape.reset();
			form.appendToPathForRuntime(runtime, shape);

			if (isCurrent && !isGuide)
			{
				g2.setColor(_colorActive);
				g2.setStroke(_stroke);
				g2.draw(shape);
				g2.fill(shape);
			}
			else if (isCurrent)
			{
				g2.setColor(_colorGuide);
				g2.setStroke(_guideStroke);
				g2.draw(shape);
			}
			else
			{
				g2.setColor(_colorShape);
				g2.setStroke(_stroke);
				g2.fill(shape);
				g2.draw(shape);
			}
		}

		g2.dispose();
	}

	@Override
	public void onError(final ProjectRuntime runtime, final Evaluable instruction, final RuntimeError error)
	{
		_failedInstructions.add(instruction);
		_errorMap.put(instruction, error);
	}

	@Override
	public void onBeginEvaluation(final ProjectRuntime runtime)
	{
		_recordedInstructions.clear();
		_failedInstructions.clear();
		_collectedShapes.clear();

		final Vec2i size = runtime.getSize();
		if (size.x != _currentSize.x || size.y != _currentSize.y)
		{
			_instructionBitmaps.clear();
			_currentSize.set(size);
			final double scale = Math.min(1.0 * _maxSize.x / _currentSize.x, 1.0 * _maxSize.y / _currentSize.y);
			final int width = (int) Math.round(scale * _currentSize.x);
			final int height = (int) Math.round(scale * _currentSize.y);
			_currentScaledSize.set(width, height);
		}

	}

	@Override
	public void onFinishEvaluation(final ProjectRuntime runtime)
	{
		final Iterator<Evaluable> iterator = _instructionBitmaps.keySet().iterator();
		while (iterator.hasNext())
		{
			final Evaluable e = iterator.next();
			if (!_recordedInstructions.contains(e))
			{
				iterator.remove();
			}
		}

		_pathPool.release();
		_redraw = false;

		for (int i = 0; i < _listeners.size(); i++)
		{
			_listeners.get(i).onCollectionCompleted(this);
		}
	}

	private BufferedImage getBufferedImage(final Evaluable instruction)
	{
		if (!_instructionBitmaps.containsKey(instruction))
		{
			_instructionBitmaps.put(instruction, new BufferedImage(_currentScaledSize.x, _currentScaledSize.y,
			                                                       BufferedImage.TYPE_4BYTE_ABGR));
		}
		return _instructionBitmaps.get(instruction);
	}

	public void requireRedraw()
	{
		_redraw = true;
	}

	public BufferedImage getImageOf(final Instruction instruction)
	{
		return _instructionBitmaps.get(instruction);
	}

	public boolean hasFailed(final Instruction instruction)
	{
		return _failedInstructions.contains(instruction);
	}

	public boolean hasBeenEvaluated(final Instruction instruction)
	{
		return _recordedInstructions.contains(instruction);
	}

	public int getWidth()
	{
		return _currentScaledSize.x;
	}

	public int getHeight()
	{
		return _currentScaledSize.y;
	}

	public RuntimeError getError(final Evaluable instruction)
	{
		return _errorMap.get(instruction);
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
				final GeneralPath.Double shape = _pathPool.take();
				shape.reset();
				form.appendToPathForRuntime(runtime, shape);
				_collectedShapes.add(shape);
			}
		}
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

}
