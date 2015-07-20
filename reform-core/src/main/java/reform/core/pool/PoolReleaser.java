package reform.core.pool;


public interface PoolReleaser<E> {
    void release(E e);
}
