package reform.stage.elements.outline;

import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.SnapPoint;

public class IntersectionSnapPointPool
{
	private final Pool<IntersectionSnapPoint> _pool = new SimplePool<>(IntersectionSnapPoint::new);

	public IntersectionSnapPoint create(final Entity entityA, final Entity entityB, final int index, final Vec2 pos)
	{

		final IntersectionSnapPoint p = _pool.take();
		p.reset(entityA, entityB, index, pos);

		return p;
	}

	public void release()
	{
		_pool.release();
	}

	public static SnapPoint copyIfNeeded(final SnapPoint p)
	{
		if (p instanceof IntersectionSnapPoint)
		{
			final IntersectionSnapPoint i = (IntersectionSnapPoint) p;
			final IntersectionSnapPoint copy = new IntersectionSnapPoint();

			copy.reset(i._entityA, i._entityB, i._index, i._position);

			return copy;
		}
		else
		{
			return p;
		}
	}
}
