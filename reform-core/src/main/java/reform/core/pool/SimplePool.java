package reform.core.pool;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimplePool<E> implements Pool<E> {
	private final PoolFactory<E> _factory;

	private final ConcurrentLinkedQueue<E> _inUse = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<E> _notInUse = new ConcurrentLinkedQueue<>();

	public SimplePool(final PoolFactory<E> factory) {
		_factory = factory;
	}

	@Override
	public void release() {
		_notInUse.addAll(_inUse);
		_inUse.clear();
	}

	@Override
	public E take() {
		E element;

		if ((element = _notInUse.poll()) == null) {
			element = _factory.create();
		}

		_inUse.add(element);
		return element;
	}

	@Override
	public int size() {
		return _inUse.size() + _notInUse.size();
	}
}