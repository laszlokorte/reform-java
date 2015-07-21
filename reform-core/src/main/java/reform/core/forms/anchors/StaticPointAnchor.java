package reform.core.forms.anchors;

import reform.core.forms.relations.StaticPoint;
import reform.core.runtime.Runtime;
import reform.identity.IdentityToken;
import reform.naming.Name;

public class StaticPointAnchor extends BaseAnchor
{

	private final StaticPoint _point;

	public StaticPointAnchor(final IdentityToken id, final Name name, final StaticPoint point)
	{
		super(id, name);
		_point = point;
	}

	@Override
	public void translate(final Runtime runtime, final double x, final double y)
	{
		final double newX = _point.getXValueForRuntime(runtime) + x;
		final double newY = _point.getYValueForRuntime(runtime) + y;
		_point.setForRuntime(runtime, newX, newY);
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return _point.getXValueForRuntime(runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return _point.getYValueForRuntime(runtime);
	}

}
