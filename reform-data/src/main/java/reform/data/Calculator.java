package reform.data;


public class Calculator {
	public static Value modulo(final Value lhs, final Value rhs)
			throws SemanticException {
		if (lhs.getType() == Value.Type.String
				|| rhs.getType() == Value.Type.String) {
			throw new SemanticException();
		} else if (lhs.getType() == Value.Type.Double
				|| rhs.getType() == Value.Type.Double) {
			return new Value(lhs.getDouble() * rhs.getDouble());
		} else {
			return new Value(lhs.getInteger() * rhs.getInteger());
		}
	}

	public static Value add(final Value lhs, final Value rhs) {
		if (lhs.getType() == Value.Type.String
				|| rhs.getType() == Value.Type.String) {
			return new Value(lhs.getString() + rhs.getString());
		} else if (lhs.getType() == Value.Type.Double
				|| rhs.getType() == Value.Type.Double) {
			return new Value(lhs.getDouble() + rhs.getDouble());
		} else {
			return new Value(lhs.getInteger() + rhs.getInteger());
		}
	}

	public static Value subtract(final Value lhs, final Value rhs)
			throws SemanticException {
		if (lhs.getType() == Value.Type.String
				|| rhs.getType() == Value.Type.String) {
			throw new SemanticException();
		} else if (lhs.getType() == Value.Type.Double
				|| rhs.getType() == Value.Type.Double) {
			return new Value(lhs.getDouble() - rhs.getDouble());
		} else {
			return new Value(lhs.getInteger() - rhs.getInteger());
		}
	}

	public static Value devide(final Value lhs, final Value rhs)
			throws SemanticException {
		if (lhs.getType() == Value.Type.String
				|| rhs.getType() == Value.Type.String) {
			throw new SemanticException();
		} else if (lhs.getType() == Value.Type.Double
				|| rhs.getType() == Value.Type.Double) {
			return new Value(lhs.getDouble() / rhs.getDouble());
		} else {
			return new Value(lhs.getInteger() / rhs.getInteger());
		}
	}

	public static Value multiply(final Value lhs, final Value rhs)
			throws SemanticException {
		if (lhs.getType() == Value.Type.String
				&& rhs.getType() == Value.Type.Intenger) {
			return new Value(String.format("%0" + rhs.getInteger() + "d", 0)
					.replace("0", lhs.getString()));

		} else if (lhs.getType() == Value.Type.Intenger
				&& rhs.getType() == Value.Type.String) {
			return new Value(String.format("%0" + lhs.getInteger() + "d", 0)
					.replace("0", rhs.getString()));
		} else if (lhs.getType() == Value.Type.String
				&& rhs.getType() == Value.Type.String) {
			throw new SemanticException();
		} else if (lhs.getType() == Value.Type.Double
				|| rhs.getType() == Value.Type.Double) {
			return new Value(lhs.getDouble() / rhs.getDouble());
		} else {
			return new Value(lhs.getInteger() / rhs.getInteger());
		}
	}
}