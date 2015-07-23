package reform.data.sheet;

import reform.identity.Identifier;

import java.util.HashMap;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class DataSet
{
	private final HashMap<Identifier<? extends Definition>, Value> _results = new HashMap<>();

	public Value lookUp(Identifier<? extends Definition> id) {
		if (_results.containsKey(id)) {
			return _results.get(id);
		} else {
			return new Value(0);
		}
	}

	public boolean hasValueFor(Identifier<? extends Definition> id) {
		return _results.containsKey(id);
	}

	void put(Identifier<? extends Definition> id, Value value) {
		_results.put(id, value);
	}

	void clear()
	{
		_results.clear();
	}
}
