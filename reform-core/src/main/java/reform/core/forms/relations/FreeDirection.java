package reform.core.forms.relations;

import reform.core.runtime.Runtime;
import reform.core.runtime.relations.Direction;

public enum FreeDirection implements Direction
{

	Free
			{
				@Override
				public double getAdjustedXForRuntime(final Runtime runtime, final double anchorX, final double
						anchorY, final double x, final double y)
				{
					return x;
				}

				@Override
				public double getAdjustedYForRuntime(final Runtime runtime, final double anchorX, final double
						anchorY, final double x, final double y)
				{
					return y;
				}
			}

}
