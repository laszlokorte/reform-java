package reform.core.runtime;

import java.util.ArrayList;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.core.runtime.stack.Stack;
import reform.data.ExpressionContext;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

public class ProjectRuntime implements Runtime {
	public static interface Listener {
		public void onBeginEvaluation(ProjectRuntime runtime);

		public void onFinishEvaluation(ProjectRuntime runtime);

		public void onEvalInstruction(ProjectRuntime runtime,
				Evaluatable instruction);

		public void onPopScope(ProjectRuntime runtime,
				FastIterable<Identifier<? extends Form>> ids);

		public void onError(ProjectRuntime runtime, Evaluatable instruction,
				Error error);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final Project _project;
	private final Vec2i _size = new Vec2i();
	private final Stack _stack = new Stack();
	private boolean _stopped = false;

	public ProjectRuntime(final Project project, final Vec2i size) {
		_project = project;
		_size.set(size);
	}

	@Override
	public void begin() {
		_stopped = false;
		_stack.clear();

		synchronized (_listeners) {
			for (int i = 0; i < _listeners.size(); i++) {
				_listeners.get(i).onBeginEvaluation(this);
			}
		}
	}

	@Override
	public void finish() {
		if (!_stack.isEmpty()) {
			System.err.println(String.format("[Out] %s", "Stack is not empty"));
		}

		if (!_stopped) {
			synchronized (_listeners) {
				for (int i = 0; i < _listeners.size(); i++) {
					_listeners.get(i).onFinishEvaluation(this);
				}
			}
			_stopped = true;
		}
	}

	@Override
	public void beforeEval(final Evaluatable instruction) {

	}

	@Override
	public void afterEval(final Evaluatable instruction) {

		synchronized (_listeners) {
			for (int i = 0; i < _listeners.size(); i++) {
				_listeners.get(i).onEvalInstruction(this, instruction);
			}
		}
	}

	@Override
	public void pushScope() {
		_stack.pushFrame();
		// System.out.println(String.format("[Out] %s", "push Scope"));
	}

	@Override
	public void popScope() {
		final FastIterable<Identifier<? extends Form>> ids = _stack
				.getForms();
		synchronized (_listeners) {
			for (int i = 0; i < _listeners.size(); i++) {
				_listeners.get(i).onPopScope(this, ids);
			}
		}
		_stack.popFrame();
		// System.out.println(String.format("[Out] %s", "pop Scope"));

	}

	@Override
	public void declare(final Form form) {
		_stack.declare(form);
	}

	@Override
	public Form get(final Identifier<? extends Form> id) {
		return _stack.get(id);
	}

	@Override
	public long get(final Identifier<? extends Form> id, final int offset) {
		return _stack.getData(id, offset);
	}

	@Override
	public void set(final Identifier<? extends Form> id, final int offset,
			final long value) {
		_stack.setData(id, offset, value);
	}

	@Override
	public void reportError(final Evaluatable instruction, final Error error) {
		synchronized (_listeners) {

			for (int i = 0; i < _listeners.size(); i++) {
				_listeners.get(i).onError(this, instruction, error);
			}
		}
	}

	@Override
	public boolean shouldStop() {
		return _stopped;
	}

	@Override
	public Runtime getSubroutine(final Identifier<? extends Picture> id) {
		return new ProjectRuntime(_project, _project.getPicture(id).getSize());
	}

	@Override
	public ExpressionContext getExpressionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addListener(final Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener) {
		_listeners.remove(listener);
	}

	@Override
	public Vec2i getSize() {
		return _size;
	}

	@Override
	public FastIterable<Identifier<? extends Form>> getStackIterator() {
		return _stack;
	}

	public void setSize(final Vec2i size) {
		_size.set(size);
	}

	public void stop() {
		synchronized (_listeners) {
			_stopped = true;
		}
	}
}