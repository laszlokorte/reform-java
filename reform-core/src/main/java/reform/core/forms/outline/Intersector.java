package reform.core.forms.outline;

import reform.core.runtime.Runtime;
import reform.math.Vec2;
import reform.math.Vector;

public final class Intersector {
	public static double intersectXForRuntime(final Runtime runtime,
			final Outline a, final Outline b, final int index) {
		final Vec2 intersection = intersectForRuntime(runtime, a, b, index);

		if (intersection != null) {
			return intersection.x;
		} else {
			return Double.NaN;
		}
	}

	public static double intersectYForRuntime(final Runtime runtime,
			final Outline a, final Outline b, final int index) {
		final Vec2 intersection = intersectForRuntime(runtime, a, b, index);

		if (intersection != null) {
			return intersection.y;
		} else {
			return Double.NaN;
		}
	}

	private static Vec2 intersectForRuntime(final Runtime runtime,
			final Outline a, final Outline b, final int index) {
		if (a instanceof LineOutline && b instanceof LineOutline) {
			return intersectLineLine(runtime, (LineOutline) a, (LineOutline) b);
		} else if (a instanceof CircleOutline && b instanceof CircleOutline) {
			return intersectCircleCircle(runtime, (CircleOutline) a,
					(CircleOutline) b, index);
		} else if (a instanceof LineOutline && b instanceof CircleOutline) {
			return intersectLineCircle(runtime, (LineOutline) a,
					(CircleOutline) b, index);
		} else if (a instanceof CircleOutline && b instanceof LineOutline) {
			return intersectLineCircle(runtime, (LineOutline) b,
					(CircleOutline) a, index);
		} else {
			return null;
		}
	}

	private static Vec2 intersectLineLine(final Runtime runtime,
			final LineOutline a, final LineOutline b) {
		final double aX0 = a.getFromXForRuntime(runtime);
		final double aY0 = a.getFromYForRuntime(runtime);

		final double aX1 = a.getToXForRuntime(runtime);
		final double aY1 = a.getToYForRuntime(runtime);

		final double bX0 = b.getFromXForRuntime(runtime);
		final double bY0 = b.getFromYForRuntime(runtime);

		final double bX1 = b.getToXForRuntime(runtime);
		final double bY1 = b.getToYForRuntime(runtime);

		return Vector.intersectionForLines(aX0, aY0, aX1, aY1, bX0, bY0, bX1,
				bY1);
	}

	private static Vec2 intersectCircleCircle(final Runtime runtime,
			final CircleOutline a, final CircleOutline b, final int index) {
		final double centerAX = a.getCenterXForRuntime(runtime);
		final double centerAY = a.getCenterYForRuntime(runtime);
		final double centerBX = b.getCenterXForRuntime(runtime);
		final double centerBY = b.getCenterYForRuntime(runtime);
		final double radiusA = a.getRadiusForRuntime(runtime);
		final double radiusB = b.getRadiusForRuntime(runtime);

		return Vector.intersectionForCircles(centerAX, centerAY, radiusA,
				centerBX, centerBY, radiusB, index);
	}

	private static Vec2 intersectLineCircle(final Runtime runtime,
			final LineOutline a, final CircleOutline b, final int index) {
		final double x1 = a.getFromXForRuntime(runtime);
		final double y1 = a.getFromYForRuntime(runtime);
		final double x2 = a.getToXForRuntime(runtime);
		final double y2 = a.getToYForRuntime(runtime);

		final double centerX = b.getCenterXForRuntime(runtime);
		final double centerY = b.getCenterYForRuntime(runtime);
		final double radius = b.getRadiusForRuntime(runtime);

		return Vector.intersectionForCircleLine(x1, y1, x2, y2, centerX,
				centerY, radius, index);
	}

}
