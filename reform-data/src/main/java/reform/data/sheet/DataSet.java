package reform.data.sheet;

import reform.identity.Identifier;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class DataSet
{
	private final HashMap<Identifier<? extends Definition>, Value> _results = new HashMap<>();
	private final HashSet<Identifier<? extends Definition>> _errors = new HashSet<>();

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

	public boolean hasError(Identifier<? extends Definition> id) {
		return _errors.contains(id);
	}

	void put(Identifier<? extends Definition> id, Value value) {
		_results.put(id, value);
	}


	void markError(Identifier<? extends Definition> id) {
		_errors.add(id);
	}

	void clear()
	{
		_errors.clear();
		_results.clear();
	}
}
