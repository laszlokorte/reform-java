package reform.math;

public final class Vector {

	public static final double EPSILON = 0.000001;

	/**
	 * Return the length of the given vector.
	 */
	public static double length(final double x, final double y) {
		return Math.sqrt(length2(x, y));
	}

	/**
	 * Return the square of the length of the given vector.
	 */
	public static double length2(final double x, final double y) {
		return x * x + y * y;
	}

	/**
	 * Return the distance between the two given points.
	 */
	static public double distance(final double aX, final double aY,
			final double bX, final double bY) {
		final double dx = aX - bX;
		final double dy = aY - bY;
		return Vector.length(dx, dy);
	}

	/**
	 * Return the square of the distance between the two given points.
	 */
	static public double distance2(final double aX, final double aY,
			final double bX, final double bY) {
		final double dx = aX - bX;
		final double dy = aY - bY;
		return Vector.length2(dx, dy);
	}

	/**
	 * Return the dot product of the two given vectors.
	 */
	static public double dot(final double aX, final double aY, final double bX,
			final double bY) {

		return aX * bX + aY * bY;
	}

	/**
	 * Return the positive counter clockwise angle between the given vector and
	 * the positive x axis in radians.
	 */
	static public double angleOf(final double x, final double y) {
		return Math.atan2(-y, -x) + Math.PI;
	}

	/**
	 * Return the x component of the projection of the source vector projected
	 * onto the target vector.
	 */
	static public double projectionX(final double sourceX, final double sourceY,
			final double targetX, final double targetY) {
		if (targetX == 0 && targetY == 0) {
			return sourceX;
		}

		final double dirLength2 = targetX * targetX + targetY * targetY;

		final double dot = sourceX * targetX + sourceY * targetY;

		return dot * targetX / dirLength2;
	}

	/**
	 * Return the y component of the projection of the source vector projected
	 * onto the target vector.
	 */
	static public double projectionY(final double sourceX, final double sourceY,
			final double targetX, final double targetY) {
		if (targetX == 0 && targetY == 0) {
			return sourceY;
		}

		final double dirLength2 = targetX * targetX + targetY * targetY;

		final double dot = sourceX * targetX + sourceY * targetY;

		return dot * targetY / dirLength2;
	}

	/**
	 * Return the x component of the given vector rotated by 90°.
	 */
	public static double orthogonalX(final double x, final double y) {
		return -y;
	}

	/**
	 * Return the y component of the given vector rotated by 90°.
	 */
	public static double orthogonalY(final double x, final double y) {
		return x;
	}

	/**
	 * Return the x component of the given vector rotated by the given angle in
	 * radians.
	 */
	public static double getRotatedX(final double x, final double y,
			final double angle) {
		return x * Math.cos(angle) - y * Math.sin(angle);
	}

	/**
	 * Return the y component of the given vector rotated by the given angle in
	 * radians.
	 */
	public static double getRotatedY(final double x, final double y,
			final double angle) {
		return y * Math.cos(angle) + x * Math.sin(angle);

	}

	/**
	 * Return the angle of the vector from the given center to the given point.
	 * The angle is measured in radians clockwise from the positive x axis.
	 */
	public static double angle(final double x, final double y,
			final double centerX, final double centerY) {
		return angleOf(x - centerX, -(y - centerY));
	}

	/**
	 * Return if the given point lies on the line between the other two given
	 * points.
	 */
	public static boolean isBetween(final double pX, final double pY,
			final double aX, final double aY, final double bX, final double bY,
			final double eps) {
		final double delta = Vector.distance(pX, pY, aX, aY)
				+ Vector.distance(pX, pY, bX, bY)
				- Vector.distance(aX, aY, bX, bY);
		return -eps < delta && delta < eps;
	}

	private static double sign(final double p1x, final double p1y,
			final double p2x, final double p2y, final double p3x,
			final double p3y) {
		return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
	}

	public static boolean isInTriangle(final double px, final double py,
			final double t1x, final double t1y, final double t2x,
			final double t2y, final double t3x, final double t3y) {
		boolean b1, b2, b3;

		b1 = sign(px, py, t1x, t1y, t2x, t2y) < 0.0f;
		b2 = sign(px, py, t2x, t2y, t3x, t3y) < 0.0f;
		b3 = sign(px, py, t3x, t3y, t1x, t1y) < 0.0f;

		return b1 == b2 && b2 == b3;
	}

	public static boolean isLeft(final double ax, final double ay,
			final double bx, final double by, final double px,
			final double py) {
		return (bx - ax) * (py - ay) - (by - ay) * (px - ax) > 0;
	}

	public static double lerp(final double a, final double b, final double t) {
		return a + (b - a) * t;
	}

	public static Vec2 intersectionForLines(final double x1, final double y1,
			final double x2, final double y2, final double x3, final double y3,
			final double x4, final double y4) {
		// line segment from (x1,y1) to (x2,y2) intersecting segment from
		// (x3,y3) to (x4,y4)
		// ref http://paulbourke.net/geometry/pointlineplane/

		final double d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

		final double na = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		final double nb = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);

		if (na == 0 && nb == 0 && d == 0) {
			return null; // lines are coincident
		}
		if (d == 0) {
			return null; // lines are parallel
		}

		final double ua = na / d;
		final double ub = nb / d;

		if (ua < EPSILON || ua > 1 - EPSILON || ub < EPSILON
				|| ub > 1 - EPSILON) {
			return null; // "intersection is beyond end of segment"
		}

		return new Vec2(lerp(x1, x2, ua), lerp(y1, y2, ua));
	}

	public static double circumference(final double radius) {
		return Math.PI * 2 * radius;
	}

	public static Vec2 intersectionForCircles(final double centerAX,
			final double centerAY, final double radiusA, final double centerBX,
			final double centerBY, final double radiusB, final int index) {
		if (index > 1) {
			return null;
		}

		final double dx = centerBX - centerAX;
		final double dy = centerBY - centerAY;
		final double d = distance(centerAX, centerAY, centerBX, centerBY);

		if (d == 0) {
			return null;
		}
		if (d > radiusA + radiusB) {
			return null;
		} // circles are separate
		if (d < Math.abs(radiusA - radiusB)) {
			return null;
		} // circles are nested

		final double a = (radiusA * radiusA - radiusB * radiusB + d * d)
				/ (2 * d);
		final double x3 = centerAX + dx * a / d;
		final double y3 = centerAY + dy * a / d;

		final double hh = radiusA * radiusA - a * a;
		if (hh < 0) {
			return null;
		}

		final double h = Math.sqrt(hh);
		final double rx = -dy * h / d;
		final double ry = dx * h / d;

		if (index == 0) {
			return new Vec2(x3 + rx, y3 + ry);
		} else if (Math.abs(d - (radiusA + radiusB)) > EPSILON) {
			return new Vec2(x3 - rx, y3 - ry);
		} else {
			return null;
		}
	}

	public static Vec2 intersectionForCircleLine(final double ax,
			final double ay, final double bx, final double by,
			final double centerX, final double centerY, final double radius,
			int index) {
		if (index > 1) {
			return null;
		}

		final double a = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		final double b = 2
				* ((bx - ax) * (ax - centerX) + (by - ay) * (ay - centerY));
		final double c = centerX * centerX + centerY * centerY + ax * ax
				+ ay * ay - 2 * (centerX * ax + centerY * ay) - radius * radius;

		double det = b * b - 4 * a * c;
		if (Math.abs(det) < 1e-8) {
			det = 0;
		}
		if (det < 0) {
			return null;
		}

		final double u1 = (-b + Math.sqrt(det)) / (2 * a);
		final double u2 = (-b - Math.sqrt(det)) / (2 * a);

		if (det == 0 && index == 1) {
			return null;
		}
		if (u1 >= 0.001 && u1 <= 0.999) {
			if (index == 0) {
				return new Vec2(lerp(ax, bx, u1), lerp(ay, by, u1));
			} else {
				index--;
			}
		}
		if (u2 >= 0.001 && u2 <= 0.999) {
			if (index == 0) {
				return new Vec2(lerp(ax, bx, u2), lerp(ay, by, u2));
			}
		}

		return null;
	}

	public static double inStepsOf(final double value, final double stepSize) {
		return Math.floor(value / stepSize) * stepSize;
	}
}