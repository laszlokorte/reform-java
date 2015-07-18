package reform.commands;

import java.util.ArrayList;

/**
 * A commander coordinates the execution of commands and keeps track of the
 * history of executed commands.
 */
public class Commander {
	private final ArrayList<Command> _undoStack = new ArrayList<>();
	private final ArrayList<Command> _redoStack = new ArrayList<>();

	/**
	 * Execute the given command
	 *
	 * @param command
	 *            the command to execute.
	 */
	public void execute(final Command command) {
		_redoStack.clear();
		command.execute();
		_undoStack.add(command);
	}

	/**
	 * Re execute the given command
	 *
	 * @param command
	 *            the command to execute, must be the same as the previously
	 *            executed command.
	 */
	public void amend(final AmendableCommand command) {
		if (_undoStack.isEmpty() || _last() != command) {
			throw new IllegalStateException("Can not amend.");
		}

		command.amend();
	}

	/**
	 * Check if there is a previous executed command that can be reversed.
	 *
	 * @return if the previous executed command can be reversed.
	 */
	public boolean canUndo() {
		return !_undoStack.isEmpty();
	}

	/**
	 * Check if there is a reversed command the can be executed again
	 *
	 * @return if an reversed command can be re executed.
	 */
	public boolean canRedo() {
		return !_redoStack.isEmpty();
	}

	/**
	 * Reverse the last executed command.
	 */
	public void undo() {
		if (!canUndo()) {
			throw new IllegalStateException("Can not undo.");
		}
		final Command c = _undoStack.remove(_undoStack.size() - 1);
		final Command inverse = c.getInverse();

		inverse.execute();
		_redoStack.add(inverse);
	}

	/**
	 * Execute the last reversed command.
	 */
	public void redo() {
		if (!canRedo()) {
			throw new IllegalStateException("Can not redo.");
		}

		final Command c = _redoStack.remove(_redoStack.size() - 1);
		final Command inverse = c.getInverse();

		inverse.execute();
		_undoStack.add(inverse);
	}

	/**
	 * Check if the given command can be executed.
	 *
	 * @param command
	 *            the command to check for.
	 * @return if the command can be executed.
	 */
	public boolean canExecute(final Command command) {
		return command.canExecute();
	}

	private Command _last() {
		return _undoStack.get(_undoStack.size() - 1);
	}
}
