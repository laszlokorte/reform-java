package reform.evented.commands;

import reform.commands.AmendableCommand;
import reform.commands.Command;
import reform.commands.Commander;

import java.util.ArrayList;

public class EventedCommander
{

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final Commander _commander;
	public EventedCommander(final Commander commander)
	{
		_commander = commander;
	}

	public void execute(final Command command)
	{
		_commander.execute(command);

		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onCommand(this);
		}
	}

	public void amend(final AmendableCommand command)
	{
		_commander.amend(command);
	}

	public void undo()
	{
		_commander.undo();

		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onCommand(this);
		}
	}

	public void redo()
	{
		_commander.redo();

		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onCommand(this);
		}
	}

	public boolean canExecute(final Command command)
	{
		return _commander.canExecute(command);
	}

	public boolean canUndo()
	{
		return _commander.canUndo();
	}

	public boolean canRedo()
	{
		return _commander.canRedo();
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public interface Listener
	{
		void onCommand(EventedCommander commander);
	}
}
