package reform.stage.elements.outline;

import reform.core.forms.Form;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vector;

public abstract class EntityOutline {

	private final static Vec2[] _empty = new Vec2[0];
	private final static Vec2[] _one = new Vec2[1];
	private final static Vec2[] _two = new Vec2[2];

	public static Vec2[] intersect(final EntityOutline a, final EntityOutline b) {
		if (a instanceof Line && b instanceof Line) {
			return intersectLineLine((Line) a, (Line) b);
		} else if (a instanceof Circle && b instanceof Circle) {
			return intersectCircleCircle((Circle) a, (Circle) b);
		} else if (a instanceof Line && b instanceof Circle) {
			return intersectLineCircle((Line) a, (Circle) b);
		} else if (a instanceof Circle && b instanceof Line) {
			return intersectLineCircle((Line) b, (Circle) a);
		} else {
			return _empty;
		}
	}

	private static Vec2[] intersectLineCircle(final Line line,
			final Circle circle) {
		final Vec2 lineP1 = line._pointA;
		final Vec2 lineP2 = line._pointB;

		final Vec2 circleCenter = circle._center;
		final double circleRadius = circle._radius;

		_two[0] = Vector.intersectionForCircleLine(lineP1.x, lineP1.y,
				lineP2.x, lineP2.y, circleCenter.x, circleCenter.y,
				circleRadius, 0);
		_two[1] = Vector.intersectionForCircleLine(lineP1.x, lineP1.y,
				lineP2.x, lineP2.y, circleCenter.x, circleCenter.y,
				circleRadius, 1);

		return _two;
	}

	private static Vec2[] intersectLineLine(final Line lineA, final Line lineB) {
		final Vec2 pA1 = lineA._pointA;
		final Vec2 pA2 = lineA._pointB;
		final Vec2 pB1 = lineB._pointA;
		final Vec2 pB2 = lineB._pointB;

		_one[0] = Vector.intersectionForLines(pA1.x, pA1.y, pA2.x, pA2.y,
				pB1.x, pB1.y, pB2.x, pB2.y);

		return _one;
	}

	private static Vec2[] intersectCircleCircle(final Circle circleA,
			final Circle circleB) {
		final Vec2 centerA = circleA._center;
		final double radiusA = circleA._radius;
		final Vec2 centerB = circleB._center;
		final double radiusB = circleB._radius;

		_two[0] = Vector.intersectionForCircles(centerA.x, centerA.y, radiusA,
				centerB.x, centerB.y, radiusB, 0);
		_two[1] = Vector.intersectionForCircles(centerA.x, centerA.y, radiusA,
				centerB.x, centerB.y, radiusB, 1);

		return _two;
	}

	private final Identifier<? extends Form> _formId;

	Identifier<? extends Form> getFormId() {
		return _formId;
	}

	private EntityOutline(final Identifier<? extends Form> formId) {
		_formId = formId;
	}

	public static class Null extends EntityOutline {

		public Null(final Identifier<? extends Form> formId) {
			super(formId);
		}

	}

	public static class Line extends EntityOutline {

		private final Vec2 _pointA = new Vec2();
		private final Vec2 _pointB = new Vec2();

		public Line(final Identifier<? extends Form> formId) {
			super(formId);
		}

		public void update(final double aX, final double aY, final double bX,
				final double bY) {
			_pointA.set(aX, aY);
			_pointB.set(bX, bY);
		}

	}

	public static class Circle extends EntityOutline {

		private final Vec2 _center = new Vec2();
		private double _radius;

		public Circle(final Identifier<? extends Form> formId) {
			super(formId);
		}

		public void update(final double cX, final double cY, final double radius) {
			_center.set(cX, cY);
			_radius = radius;
		}

	}
}
