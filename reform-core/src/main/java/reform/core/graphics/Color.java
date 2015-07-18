package reform.core.graphics;

public class Color {
	private int _argb;

	public Color() {
		_argb = 0xff000000;
	}

	public Color(final int argb) {
		_argb = argb;
	}

	public int getARGB() {
		return _argb;
	}

	public void setARGB(final int argb) {
		_argb = argb;
	}

	public double getAlpha() {
		return (0xff & _argb >> 24) / 255.0;
	}

	public double getRed() {
		return (0xff & _argb >> 16) / 255.0;
	}

	public double getGreen() {
		return (0xff & _argb >> 8) / 255.0;
	}

	public double getBlue() {
		return (0xff & _argb) / 255.0;
	}

	@Override
	public String toString() {
		return "argb(" + getAlpha() + ", " + getRed() + ", " + getGreen()
				+ ", " + getBlue() + ", " + ")";
	}
}
