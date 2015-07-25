package reform.stage.tooling;

import reform.core.procedure.instructions.Instruction;

import java.util.ArrayList;

public class InstructionFocus
{
	public interface Listener
	{
		void onFocusChanged(InstructionFocus focus);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private Instruction _instruction;

	public InstructionFocus()
	{
		// TODO Auto-generated constructor stub
	}

	public void setFocus(final Instruction instruction)
	{
		if (_instruction != instruction)
		{
			_instruction = instruction;
			for (int i = 0, j = _listeners.size(); i < j; i++)
			{
				_listeners.get(i).onFocusChanged(this);
			}
		}
	}

	public boolean isFocused(final Instruction instruction)
	{
		return _instruction.equals(instruction);
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public Instruction getFocused()
	{
		return _instruction;
	}

	public boolean isSet()
	{
		return _instruction != null;
	}
}
