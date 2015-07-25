package reform.components.colorpicker;

import java.util.ArrayList;

public class ColorModel
{

	public void setHexARGB(final int hexARGB)
	{
		final int alpha = (hexARGB >>> 24) & 0xff;
		final int red = (hexARGB >>> 16) & 0xff;
		final int green = (hexARGB >>> 8) & 0xff;
		final int blue = hexARGB & 0xff;

		setRGBA(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0);
	}

	public interface Listener
	{
		void onColorChange(ColorModel model);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private double _red = 0;
	private double _green = 0;
	private double _blue = 0;
	private double _alpha = 1;

	private double _hue = 0;
	private double _saturation = 0;
	private double _value = 0;

	public void setRGBA(final double r, final double g, final double b, final double a)
	{
		_alpha = a;
		setRGB(r, g, b);
	}

	public void setRGB(final double r, final double g, final double b)
	{
		_red = r;
		_green = g;
		_blue = b;

		updateHSV();

		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onColorChange(this);
		}
	}

	public void setHSVA(final double h, final double s, final double v, final double a)
	{
		_alpha = a;

		setHSV(h, s, v);
	}

	public void setHSV(final double h, final double s, final double v)
	{
		_hue = h;
		_saturation = s;
		_value = v;

		updateRGB();

		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onColorChange(this);
		}
	}

	public void setAlpha(final double alpha)
	{
		_alpha = alpha;
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onColorChange(this);
		}
	}

	private void updateRGB()
	{
		final double h = _hue;
		final double s = _saturation;
		final double v = _value;
		if (s == 0 || v == 0)
		{
			_red = _green = _blue = v;
		}
		else
		{
			final double sixHue = h % 1 * 6;
			final int index = (int) Math.floor(sixHue);
			final double fraction = sixHue - index;

			final double steady = 1 - s;
			final double falling = 1 - s * fraction;
			final double rising = 1 - s * (1 - fraction);
			final double maximum = 1;

			final double red, green, blue;

			switch (index)
			{
				case 0:
					red = maximum;
					green = rising;
					blue = steady;
					break;
				case 1:
					red = falling;
					green = maximum;
					blue = steady;
					break;
				case 2:
					red = steady;
					green = maximum;
					blue = rising;
					break;
				case 3:
					red = steady;
					green = falling;
					blue = maximum;
					break;
				case 4:
					red = rising;
					green = steady;
					blue = maximum;
					break;
				default:
					red = maximum;
					green = steady;
					blue = falling;
					break;
			}


			_red = red * v;
			_green = green * v;
			_blue = blue * v;
		}
	}


	private void updateHSV()
	{
		final double r = _red;
		final double g = _green;
		final double b = _blue;
		double h;
		final double s;
		final double v;

		final double min = Math.min(Math.min(r, g), b);
		final double max = Math.max(Math.max(r, g), b);
		v = max;

		if (max == 0)
		{
			h = s = 0;
		}
		else
		{
			final double delta = max - min;
			s = delta / max;

			if (delta == 0)
			{
				h = 0;
			}
			else if (r == max)
			{
				h = (g - b) / delta / 6;
			}
			else if (g == max)
			{
				h = (2 + (b - r) / delta) / 6;
			}
			else
			{
				h = (4 + (r - g) / delta) / 6;
			}

			if (h < 0)
			{
				h += 1;
			}
		}

		_hue = h;
		_saturation = s;
		_value = v;
	}

	public double getRed()
	{
		return _red;
	}

	public double getGreen()
	{
		return _green;
	}

	public double getBlue()
	{
		return _blue;
	}

	public double getAlpha()
	{
		return _alpha;
	}

	public double getHue()
	{
		return _hue;
	}

	public double getSaturation()
	{
		return _saturation;
	}

	public double getValue()
	{
		return _value;
	}

	public void addListener(final Listener l)
	{
		_listeners.add(l);
	}

	public void removeListener(final Listener l)
	{
		_listeners.remove(l);
	}
}
