package reform.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An {@link IdentifiableList} contains Objects which are {@link Identifiable}.
 *
 * The Objects in the list can be retrieved by there {@link Identifier}.
 *
 *
 * @param <E>
 */
public class IdentifiableList<E extends Identifiable<E>>
		implements Iterable<Identifier<? extends E>>,
		FastIterable<Identifier<? extends E>> {
	private final ArrayList<Identifier<? extends E>> _ids = new ArrayList<>();
	private final HashMap<Identifier<? extends E>, E> _mapping = new HashMap<>();

	/**
	 * Add an element to the list.
	 *
	 * @param element
	 *            The element to add.
	 */
	public <T extends E> void add(final T element) {
		final Identifier<? extends E> id = element.getId();
		assert!_mapping.containsKey(id);

		_ids.add(id);
		_mapping.put(id, element);
	}

	/**
	 * Remove an element from the list.
	 *
	 * @param element
	 *            The element to remove
	 */
	public void remove(final E element) {
		final Identifier<? extends E> id = element.getId();

		_ids.remove(id);
		_mapping.remove(id);
	}

	/**
	 * Remove the element with the given id.
	 *
	 * @param id
	 *            the Identifier of the element.
	 */
	public void removeById(final Identifier<? extends E> id) {
		final E element = _mapping.remove(id);
		assert element != null;

		_ids.remove(id);
	}

	/**
	 * Get the element with the given {@link Identifier}
	 *
	 * @param id
	 *            the {@link Identifier} of the element.
	 * @return The element with the given id.
	 */
	public E getById(final Identifier<? extends E> id) {
		return _mapping.get(id);
	}

	/**
	 * Check if the list contains an element with the given id.
	 *
	 * @param id
	 * @return if the list contains the requested element.
	 */
	public boolean contains(final Identifier<? extends E> id) {
		return _mapping.containsKey(id);
	}

	/**
	 * Get the id of the element at the given index.
	 *
	 * @param index
	 *            The index of the element.
	 * @return The id of the element.
	 */
	public Identifier<? extends E> getIdAtIndex(final int index) {
		return _ids.get(index);
	}

	/**
	 * Get the size of the list
	 *
	 * @return The size of the list.
	 */
	@Override
	public int size() {
		return _ids.size();
	}

	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		_ids.clear();
		_mapping.clear();
	}

	@Override
	public Iterator<Identifier<? extends E>> iterator() {
		return _ids.iterator();
	}

	@Override
	public Identifier<? extends E> get(final int i) {
		return getIdAtIndex(i);
	}
}
