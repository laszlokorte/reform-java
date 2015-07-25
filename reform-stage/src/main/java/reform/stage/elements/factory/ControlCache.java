package reform.stage.elements.factory;

import reform.core.runtime.Evaluable;
import reform.stage.elements.InstructionControl;

import java.util.HashMap;
import java.util.HashSet;

public class ControlCache
{
	private final HashMap<Evaluable, InstructionControl> _cache = new HashMap<>();
	private final HashSet<Evaluable> _cold = new HashSet<>();

	public void coolDown()
	{
		_cold.addAll(_cache.keySet());
	}

	public void cleanUp()
	{
		_cold.forEach(_cache::remove);
		_cold.clear();
	}

	public InstructionControl fetch(final Evaluable instruction, final ControlFactory
			factory)
	{
		if (_cache.containsKey(instruction))
		{
			_cold.remove(instruction);
			return _cache.get(instruction);
		}
		else
		{
			final InstructionControl e = factory.createEntityFor(instruction);
			_cache.put(instruction, e);
			return e;
		}
	}

}
