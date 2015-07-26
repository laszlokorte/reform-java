package reform.core.runtime;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.core.runtime.errors.RuntimeError;
import reform.core.runtime.stack.Stack;
import reform.data.sheet.DataSet;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

import java.util.ArrayList;

public class ProjectRuntime implements Runtime
{
	private final Object _lock = new Object();
	private final Project _project;
	private boolean _stopped = false;
	private int _depth = 0;
	private Frame[] _frames = new Frame[MAX_DEPTH];

	{
		for (int i = 0, j = _frames.length; i < j; i++)
		{
			_frames[i] = new Frame();
		}
	}

	public ProjectRuntime(final Project project, final Vec2i size, final DataSet dataSet)
	{
		_project = project;
		_frames[_depth]._size.set(size);
		_frames[_depth]._dataSet = dataSet;
	}

	@Override
	public Picture subCall(Identifier<? extends Picture> picId, int width, int height)
	{
		Picture picture = _project.getPicture(picId);
		if (_depth + 1 >= _frames.length || picture == null)
		{
			return null;
		}

		_depth += 1;
		_frames[_depth]._size.set(width, height);
		return picture;
	}

	@Override
	public void subEnd()
	{
		_frames[_depth]._listeners.clear();
		_depth -= 1;
	}

	@Override
	public void begin()
	{
		_stopped = false;
		_frames[_depth]._stack.clear();

		// synchronized (_lock)
		{
			for (int i = 0, j = _frames[_depth]._listeners.size(); i < j; i++)
			{
				_frames[_depth]._listeners.get(i).onBeginEvaluation(this);
			}
		}
	}

	@Override
	public void finish()
	{
		{
			if (!_frames[_depth]._stack.isEmpty())
			{
				throw new RuntimeException("a");
			}

			if (!_stopped)
			{
				// synchronized  (_lock)
				{
					for (int i = 0, j = _frames[_depth]._listeners.size(); i < j; i++)
					{
						_frames[_depth]._listeners.get(i).onFinishEvaluation(this);
					}
				}
				if (_depth == 0)
				{
					_stopped = true;
				}
			}
		}
	}

	@Override
	public void beforeEval(final Evaluable instruction)
	{

	}

	@Override
	public void afterEval(final Evaluable instruction)
	{

		// synchronized  (_lock)
		{
			for (int i = 0, j = _frames[_depth]._listeners.size(); i < j; i++)
			{
				_frames[_depth]._listeners.get(i).onEvalInstruction(this, instruction);
			}
		}
	}

	@Override
	public void pushScope()
	{
		_frames[_depth]._stack.pushFrame();
	}

	@Override
	public void popScope()
	{
		{
			final FastIterable<Identifier<? extends Form>> ids = _frames[_depth]._stack
					.getForms();
			// synchronized  (_lock)
			{
				for (int i = 0, j = _frames[_depth]._listeners.size(); i < j; i++)
				{
					_frames[_depth]._listeners.get(i).onPopScope(this, ids);
				}
			}
		}

		_frames[_depth]._stack.popFrame();

	}

	@Override
	public void declare(final Form form)
	{
		_frames[_depth]._stack.declare(form);
	}

	@Override
	public Form get(final Identifier<? extends Form> id)
	{
		return _frames[_depth]._stack.get(id);
	}

	@Override
	public long get(final Identifier<? extends Form> id, final int offset)
	{
		return _frames[_depth]._stack.getData(id, offset);
	}

	@Override
	public void set(final Identifier<? extends Form> id, final int offset, final long
			value)
	{
		_frames[_depth]._stack.setData(id, offset, value);
	}

	@Override
	public FastIterable<Identifier<? extends Form>> getStackIterator()
	{
		return _frames[_depth]._stack;
	}

	@Override
	public void reportError(final Evaluable instruction, final RuntimeError error)
	{
		// synchronized  (_lock)
		{
			for (int i = 0, j = _frames[_depth]._listeners.size(); i < j; i++)
			{
				_frames[_depth]._listeners.get(i).onError(this, instruction, error);
			}
		}
	}

	@Override
	public boolean shouldStop()
	{
		return _stopped;
	}

	@Override
	public DataSet getDataSet()
	{
		return _frames[_depth]._dataSet;
	}

	@Override
	public Vec2i getSize()
	{
		return _frames[_depth]._size;
	}

	public void setSize(final Vec2i size)
	{
		_frames[_depth]._size.set(size);
	}

	public void addListener(final Listener listener)
	{
		_frames[_depth]._listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_frames[_depth]._listeners.remove(listener);
	}

	public void stop()
	{
		// synchronized  (_lock)
		{
			_stopped = true;
		}
	}

	public int getDepth() {
		return _depth;
	}

	private static class Frame
	{
		private final ArrayList<Listener> _listeners = new ArrayList<>();
		private final Vec2i _size = new Vec2i();
		private DataSet _dataSet = null;
		private final Stack _stack = new Stack();

		Frame()
		{
		}
	}
}
