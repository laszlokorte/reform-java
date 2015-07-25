package reform.data.sheet;

import reform.identity.Identifier;

import java.util.HashMap;
import java.util.HashSet;

public class DataSet
{
	private final HashMap<Identifier<? extends Definition>, Value> _results = new
			HashMap<>();
	private final HashSet<Identifier<? extends Definition>> _errors = new HashSet<>();

	public Value lookUp(final Identifier<? extends Definition> id)
	{
		if (_results.containsKey(id))
		{
			return _results.get(id);
		}
		else
		{
			return new Value(0);
		}
	}

	public boolean hasValueFor(final Identifier<? extends Definition> id)
	{
		return _results.containsKey(id);
	}

	public boolean hasError(final Identifier<? extends Definition> id)
	{
		return _errors.contains(id);
	}

	void put(final Identifier<? extends Definition> id, final Value value)
	{
		_results.put(id, value);
	}


	void markError(final Identifier<? extends Definition> id)
	{
		_errors.add(id);
	}

	void clear()
	{
		_errors.clear();
		_results.clear();
	}
}
