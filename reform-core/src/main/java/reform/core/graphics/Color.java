package reform.core.graphics;

public class Color
{
	private static final int MASK_ALPHA = 0xff000000;
	private static final int MASK_RED = 0x00ff0000;
	private static final int MASK_GREEN = 0x0000ff00;
	private static final int MASK_BLUE = 0x000000ff;

	private int _argb;

	public Color()
	{
		_argb = 0xff000000;
	}

	public Color(final int argb)
	{
		_argb = argb;
	}

	public Color(final Color other)
	{
		_argb = other._argb;
	}

	public int getARGB()
	{
		return _argb;
	}

	public void setARGB(final int argb)
	{
		_argb = argb;
	}

	public double getAlpha()
	{
		return (0xff & _argb >> 24) / 255.0;
	}

	public double getRed()
	{
		return (0xff & _argb >> 16) / 255.0;
	}

	public double getGreen()
	{
		return (0xff & _argb >> 8) / 255.0;
	}

	public double getBlue()
	{
		return (0xff & _argb) / 255.0;
	}

	public void setAlpha(final double alpha)
	{
		_argb = (_argb & ~MASK_ALPHA) | (((int) Math.round(alpha * 255)) << 24);
	}

	public void setRed(final double red)
	{
		_argb = (_argb & ~MASK_RED) | (((int) Math.round(red * 255)) << 16);
	}

	public void setGreen(final double green)
	{
		_argb = (_argb & ~MASK_GREEN) | (((int) Math.round(green * 255)) << 8);
	}

	public void setBlue(final double blue)
	{
		_argb = (_argb & ~MASK_BLUE) | ((int) Math.round(blue * 255));
	}


	@Override
	public String toString()
	{
		return "argb(" + getAlpha() + ", " + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + ")";
	}
}
