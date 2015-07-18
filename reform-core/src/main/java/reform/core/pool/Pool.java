package reform.core.pool;

public interface Pool<E> {
	E take();

	void release();

	int size();
}
