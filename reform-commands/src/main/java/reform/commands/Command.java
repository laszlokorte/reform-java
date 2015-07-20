package reform.commands;

/**
 * A command is one atomic set of changes to be made.
 */
public interface Command
{
	/**
	 * Run the command to apply the changes it represents.
	 */
	void execute();

	/**
	 * Check if the command can be executed.
	 *
	 * @return if the command can be executed.
	 */
	boolean canExecute();

	/**
	 * Get the command representing a set of changes which inverse the changes
	 * this command represents.
	 *
	 * @return the inverse command.
	 */
	Command getInverse();

}
