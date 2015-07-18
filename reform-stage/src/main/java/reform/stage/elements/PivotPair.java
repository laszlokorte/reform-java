package reform.stage.elements;

import reform.core.runtime.relations.ReferencePoint;

public class PivotPair {
	public static enum Choice {
		Primary {
			@Override
			double getX(final PivotPair p) {
				return p._a.getX();
			}

			@Override
			double getY(final PivotPair p) {
				return p._a.getY();
			}

			@Override
			ReferencePoint createReference(final PivotPair p) {
				return p._a.createReference();
			}
		},
		Secondary {
			@Override
			double getX(final PivotPair p) {
				return p._b.getX();
			}

			@Override
			double getY(final PivotPair p) {
				return p._b.getY();
			}

			@Override
			ReferencePoint createReference(final PivotPair p) {
				return p._b.createReference();
			}
		};

		abstract double getX(PivotPair p);

		abstract double getY(PivotPair p);

		abstract ReferencePoint createReference(PivotPair p);
	}

	private final SnapPoint _b;
	private final SnapPoint _a;

	public PivotPair(final SnapPoint a, final SnapPoint b) {
		_a = a;
		_b = b;
	}

	public ReferencePoint createReference(final Choice choice) {
		return choice.createReference(this);
	}

	public double getX(final Choice choice) {
		return choice.getX(this);
	}

	public double getY(final Choice choice) {
		return choice.getY(this);
	}
}
