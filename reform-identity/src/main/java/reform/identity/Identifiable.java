package reform.identity;

/**
 *
 * An identifiable object is an Object Providing an {@link Identifier} for
 * itself.
 *
 * @param <E>
 */
public interface Identifiable<E> {
	/**
	 * Get the {@link Identifier}
	 *
	 * @return the Objects identifier
	 */
	public Identifier<? extends E> getId();
}
