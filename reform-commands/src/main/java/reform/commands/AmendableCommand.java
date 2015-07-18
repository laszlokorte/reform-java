package reform.commands;

/**
 *
 * An {@link AmendableCommand} is a {@link Command} for which there is a way to
 * execute it again after changing it's parameters just after it already has
 * been executed.
 *
 */
public interface AmendableCommand extends Command {
	void amend();

}
