package reform.stage.elements.factory;

import java.util.HashMap;
import java.util.HashSet;

import reform.core.forms.Form;
import reform.stage.elements.Entity;

public class EntityCache {
	private final HashMap<Form, Entity> _cache = new HashMap<>();
	private final HashSet<Form> _cold = new HashSet<>();

	public void coolDown() {
		_cold.addAll(_cache.keySet());
	}

	public void cleanUp() {
		for (final Form f : _cold) {
			_cache.remove(f);
		}
		_cold.clear();
	}

	public Entity fetch(final Form form, final EntityFactory factory) {
		if (_cache.containsKey(form)) {
			_cold.remove(form);
			return _cache.get(form);
		} else {
			final Entity e = factory.createEntityFor(form);
			_cache.put(form, e);
			return e;
		}
	}

}
