package reform.stage.elements.outline;

import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.SnapPoint;

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

    public static SnapPoint copyIfNeeded(SnapPoint p) {
        if(p instanceof IntersectionSnapPoint) {
            IntersectionSnapPoint i = (IntersectionSnapPoint) p;
            IntersectionSnapPoint copy = new IntersectionSnapPoint();

            copy.reset(i._entityA, i._entityB, i._index, i._position);

            return copy;
        } else {
            return p;
        }
    }
}
