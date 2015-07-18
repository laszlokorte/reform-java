package reform.math;

/**
 * A 2-dimensional math vector containing real numbers.
 */
public class Vec2 {
	/**
	 * The x component of the vector. It is public for easy access.
	 */
	public double x;

	/**
	 * The y component of the vector. It is public for easy access.
	 */
	public double y;

	/**
	 * Create a new vector with all components set to 0.
	 */
	public Vec2() {
		x = y = 0;
	}

	/**
	 * Create a new vector as a copy of the given vector.
	 */
	public Vec2(final Vec2 other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Create a new vector with the given values.
	 *
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 */
	public Vec2(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set this vector's values to be equal to the given vector's.
	 *
	 * @param other
	 *            vector to copy the values from.
	 */
	public void set(final Vec2 other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Set this vector's components to the given values x and y.
	 *
	 * @param x
	 *            The new x value
	 * @param y
	 *            The new y value
	 */
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

}
