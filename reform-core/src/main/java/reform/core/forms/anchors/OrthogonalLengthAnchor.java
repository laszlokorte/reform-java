package reform.core.forms.anchors;

import reform.core.forms.relations.StaticLength;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.IdentityToken;
import reform.math.Vector;
import reform.naming.Name;

public class OrthogonalLengthAnchor extends BaseAnchor {

	private final StaticLength _offset;
	private final ReferencePoint _pointA;
	private final ReferencePoint _pointB;

	public OrthogonalLengthAnchor(final IdentityToken token, final Name name,
			final StaticLength offset, final ReferencePoint pointA,
			final ReferencePoint pointB) {
		super(token, name);
		_offset = offset;
		_pointA = pointA;
		_pointB = pointB;
	}

	@Override
	public void translate(final Runtime runtime, final double x,
			final double y) {
		final double aX = _pointA.getXValueForRuntime(runtime);
		final double aY = _pointA.getYValueForRuntime(runtime);
		final double bX = _pointB.getXValueForRuntime(runtime);
		final double bY = _pointB.getYValueForRuntime(runtime);
		final double oldOffset = _offset.getValueForRuntime(runtime);

		final double deltaX = bX - aX;
		final double deltaY = bY - aY;
		final double distance = Vector.distance(aX, aY, bX, bY);

		final double orthogonalX = -Vector.orthogonalX(deltaX, deltaY) / distance;
		final double orthogonalY = -Vector.orthogonalY(deltaX, deltaY) / distance;

		final double oldX = orthogonalX * oldOffset;
		final double oldY = orthogonalY * oldOffset;

		final double newX = oldX + x;
		final double newY = oldY + y;

		_offset.setForRuntime(runtime, Vector.dot(newX, newY, orthogonalX, orthogonalY));
	}

}