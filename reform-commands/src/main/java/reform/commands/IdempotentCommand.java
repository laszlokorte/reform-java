package reform.commands;

/**
 * A Command which can be executed multiple times without accumulate side
 * effects.
 */
public interface IdempotentCommand extends AmendableCommand
{
	@Override
	default void amend()
	{
		execute();
	}
}
