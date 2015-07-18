package reform.core.pool;

public interface Pool<E> {
	public E take();

	public void release();

	public int size();
}
