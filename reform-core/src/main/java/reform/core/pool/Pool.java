package reform.core.pool;

public interface Pool<E> {
	E take();

    void release();

    void release(PoolReleaser<E> releaser);

    void clean(PoolCleaner<E> cleaner);

    int size();
}
