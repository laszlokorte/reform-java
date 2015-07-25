package reform.core.pool;

import java.util.ArrayList;

public class SimplePool<E> implements Pool<E>
{
	private final PoolFactory<E> _factory;

	private final ArrayList<E> _inUse = new ArrayList<>();
	private final ArrayList<E> _notInUse = new ArrayList<>();

	public SimplePool(final PoolFactory<E> factory)
	{
		_factory = factory;
	}

	@Override
	public E take()
	{
		E element;

		if (_notInUse.size() == 0)
		{
			element = _factory.create();
		}
		else
		{
			element = _notInUse.remove(_notInUse.size() - 1);
		}

		_inUse.add(element);
		return element;
	}

	@Override
	public void release()
	{
		int i = _inUse.size();
		while (i-- > 0)
		{
			E e = _inUse.remove(i);
			_notInUse.add(e);
		}
	}

	@Override
	public void release(final PoolModifier<E> releaser)
	{
		int i = _inUse.size();
		while (i-- > 0)
		{
			E e = _inUse.remove(i);
			releaser.modify(e);
			_notInUse.add(e);
		}
	}

	@Override
	public void clean(final PoolModifier<E> cleaner)
	{
		int i = _notInUse.size();
		while (i-- > 0)
		{
			final E e = _notInUse.get(i);
			cleaner.modify(e);
		}
	}

	@Override
	public void eachActive(final PoolModifier<E> modifier)
	{
		int i = _inUse.size();
		while (i-- > 0)
		{
			final E e = _inUse.get(i);
			modifier.modify(e);
		}
	}

	@Override
	public int size()
	{
		return _inUse.size() + _notInUse.size();
	}
}
