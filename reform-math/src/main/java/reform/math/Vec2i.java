package reform.math;

/**
 * A 2-dimensional math vector containing natural numbers.
 */
public class Vec2i
{
	/**
	 * The x component of the vector. It is public for easy access.
	 */
	public int x;
	/**
	 * The y component of the vector. It is public for easy access.
	 */
	public int y;

	/**
	 * Create a new vector with all components set to 0.
	 */
	public Vec2i()
	{
		x = y = 0;
	}

	/**
	 * Create a new vector with the given values.
	 *
	 * @param x the x component
	 * @param y the y component
	 */
	public Vec2i(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new vector as a copy of the given vector.
	 *
	 * @param other
	 */
	public Vec2i(final Vec2i other)
	{
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * Set this vector's values to be equal to the given vector's.
	 *
	 * @param other vector to copy the values from.
	 */
	public void set(final Vec2i other)
	{
		x = other.x;
		y = other.y;
	}

	/**
	 * Set this vector's components to the given values x and y.
	 *
	 * @param x The new x value
	 * @param y The new y value
	 */
	public void set(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public Vec2i clampLow(final int x, final int y)
	{
		this.x = Math.max(this.x, x);
		this.y = Math.max(this.y, y);
		return this;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Vec2i other = (Vec2i) obj;
		return other.x == x && other.y == y;
	}
}
