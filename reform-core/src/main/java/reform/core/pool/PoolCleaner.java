package reform.core.pool;


public interface PoolCleaner<E>
{
	void clean(E e);
}
