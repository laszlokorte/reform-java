package reform.commands;

/**
 * A Command which can be executed multiple times without accumulate side
 * effects.
 */
public abstract class IdempotentBaseCommand implements AmendableCommand {
	@Override
	public void amend() {
		execute();
	}
}
