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
	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final Project _project;
	private final Vec2i _size = new Vec2i();
	private final DataSet _dataSet;
	private final Stack _stack = new Stack();
	private boolean _stopped = false;
	public ProjectRuntime(final Project project, final Vec2i size, final DataSet dataSet)
	{
		_project = project;
		_size.set(size);
		_dataSet = dataSet;
	}

	@Override
	public void begin()
	{
		_stopped = false;
		_stack.clear();

		//synchronized (_listeners)
		{
			for (int i = 0, j = _listeners.size(); i < j; i++)
			{
				_listeners.get(i).onBeginEvaluation(this);
			}
		}
	}

	@Override
	public void finish()
	{
		if (!_stack.isEmpty())
		{
			System.err.println(String.format("[Out] %s", "Stack is not empty"));
		}

		if (!_stopped)
		{
			//synchronized (_listeners)
			{
				for (int i = 0, j = _listeners.size(); i < j; i++)
				{
					_listeners.get(i).onFinishEvaluation(this);
				}
			}
			_stopped = true;
		}
	}

	@Override
	public void beforeEval(final Evaluable instruction)
	{

	}

	@Override
	public void afterEval(final Evaluable instruction)
	{

		//synchronized (_listeners)
		{
			for (int i = 0, j = _listeners.size(); i < j; i++)
			{
				_listeners.get(i).onEvalInstruction(this, instruction);
			}
		}
	}

	@Override
	public void pushScope()
	{
		_stack.pushFrame();
		// System.out.println(String.format("[Out] %s", "push Scope"));
	}

	@Override
	public void popScope()
	{
		final FastIterable<Identifier<? extends Form>> ids = _stack.getForms();
		//synchronized (_listeners)
		{
			for (int i = 0, j = _listeners.size(); i < j; i++)
			{
				_listeners.get(i).onPopScope(this, ids);
			}
		}
		_stack.popFrame();
		// System.out.println(String.format("[Out] %s", "pop Scope"));

	}

	@Override
	public void declare(final Form form)
	{
		_stack.declare(form);
	}

	@Override
	public Form get(final Identifier<? extends Form> id)
	{
		return _stack.get(id);
	}

	@Override
	public long get(final Identifier<? extends Form> id, final int offset)
	{
		return _stack.getData(id, offset);
	}

	@Override
	public void set(final Identifier<? extends Form> id, final int offset, final long
			value)
	{
		_stack.setData(id, offset, value);
	}

	@Override
	public FastIterable<Identifier<? extends Form>> getStackIterator()
	{
		return _stack;
	}

	@Override
	public void reportError(final Evaluable instruction, final RuntimeError error)
	{
		synchronized (_listeners)
		{
			for (int i = 0, j = _listeners.size(); i < j; i++)
			{
				_listeners.get(i).onError(this, instruction, error);
			}
		}
	}

	@Override
	public boolean shouldStop()
	{
		return _stopped;
	}

	@Override
	public Runtime getSubroutine(final Identifier<? extends Picture> id)
	{
		return new ProjectRuntime(_project, _project.getPicture(id).getSize(),
		                          new DataSet());
	}

	@Override
	public DataSet getDataSet()
	{
		return _dataSet;
	}

	@Override
	public Vec2i getSize()
	{
		return _size;
	}

	public void setSize(final Vec2i size)
	{
		_size.set(size);
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public void stop()
	{
		synchronized (_listeners)
		{
			_stopped = true;
		}
	}

	public interface Listener
	{
		void onBeginEvaluation(ProjectRuntime runtime);

		void onFinishEvaluation(ProjectRuntime runtime);

		void onEvalInstruction(ProjectRuntime runtime, Evaluable instruction);

		void onPopScope(ProjectRuntime runtime, FastIterable<Identifier<? extends Form>>
				ids);

		void onError(ProjectRuntime runtime, Evaluable instruction, RuntimeError error);
	}
}
