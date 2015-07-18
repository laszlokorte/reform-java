package reform.commands;

/**
 * A {@link TransactionCommand} is composed of multiple sub commands to be
 * executed in one step.
 */
public class TransactionCommand implements Command {

	private final Command[] _subCommands;

	/**
	 * Create new transaction from multiple commands.
	 * 
	 * @param commands
	 */
	public TransactionCommand(final Command... commands) {
		_subCommands = commands;
	}

	@Override
	public void execute() {
		for (int i = 0; i < _subCommands.length; i++) {
			_subCommands[i].execute();
		}
	}

	@Override
	public boolean canExecute() {
		for (int i = 0; i < _subCommands.length; i++) {
			if (!_subCommands[i].canExecute()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Command getInverse() {
		final Command[] inverse = new Command[_subCommands.length];
		for (int i = _subCommands.length - 1, j = 0; i >= 0; i--, j++) {
			inverse[j] = _subCommands[i].getInverse();
		}
		return new TransactionCommand(inverse);
	}
}
