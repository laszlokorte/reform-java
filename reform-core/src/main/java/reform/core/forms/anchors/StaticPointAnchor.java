package reform.core.forms.anchors;

import reform.core.forms.relations.StaticPoint;
import reform.core.runtime.Runtime;
import reform.identity.IdentityToken;
import reform.naming.Name;

public class StaticPointAnchor extends BaseAnchor
{

	private final StaticPoint[] _points;

	public StaticPointAnchor(final IdentityToken id, final Name name, final StaticPoint... points)
	{
		super(id, name);
		_points = points;
	}

	@Override
	public void translate(final Runtime runtime, final double x, final double y)
	{
		for (int i = 0; i < _points.length; i++)
		{
			final StaticPoint point = _points[i];

			final double newX = point.getXValueForRuntime(runtime) + x;
			final double newY = point.getYValueForRuntime(runtime) + y;
			point.setForRuntime(runtime, newX, newY);
		}
	}

}
