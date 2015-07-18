package reform.core.forms.transformation;

import reform.core.forms.relations.StaticPoint;
import reform.core.runtime.Runtime;

public class BasicTranslator implements Translator {

	private final StaticPoint[] _points;

	public BasicTranslator(final StaticPoint... points) {
		_points = points;
	}

	@Override
	public void translate(final Runtime runtime, final double deltaX,
			final double deltaY) {
		for (int i = 0; i < _points.length; i++) {
			final double x = _points[i].getXValueForRuntime(runtime);
			final double y = _points[i].getYValueForRuntime(runtime);

			_points[i].setForRuntime(runtime, x + deltaX, y + deltaY);
		}

	}

}
