package reform.stage.elements.outline;

import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.math.Vec2;
import reform.stage.elements.Entity;

public class IntersectionSnapPointPool {
    private final Pool<IntersectionSnapPoint> _pool = new SimplePool<>(() ->
            new IntersectionSnapPoint());

    public IntersectionSnapPoint create(final Entity entityA,
                                        final Entity entityB, final int index,
                                        final Vec2 pos) {

        IntersectionSnapPoint p= _pool.take();
        p.reset(entityA,entityB,index,pos);

        return p;
    }

    public void release() {
        _pool.release();
    }
}
