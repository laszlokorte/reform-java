package reform.components.colorpicker;

import java.util.ArrayList;

/**
 * Created by laszlokorte on 19.07.15.
 */
public class ColorModel {
    public static interface Listener {
        public void onColorChange(ColorModel model);
    }

    private final ArrayList<Listener> _listeners = new ArrayList<>();

    private double _red;
    private double _green;
    private double _blue;
    private double _alpha;

    private double _hue;
    private double _saturation;
    private double _value;

    public void setRGBA(double r, double g, double b, double a) {
        _red = r;
        _green = g;
        _blue = b;
        _alpha = a;

        updateHSV();

        for(int i=0,j=_listeners.size();i<j;i++) {
            _listeners.get(i).onColorChange(this);
        }
    }

    public void setHSVA(double h, double s, double v, double a) {
        _hue = h;
        _saturation = s;
        _value = v;
        _alpha = a;

        updateRGB();

        for(int i=0,j=_listeners.size();i<j;i++) {
            _listeners.get(i).onColorChange(this);
        }
    }


    private void updateRGB() {
        double h = _hue, s = _saturation, v = _value, a = _alpha;
        if (s == 0 || v == 0) {
            _red = _green = _blue= v;
            return;
        }
        else {
            double sixHue = h % 1 * 6;
            int index = (int) Math.floor(sixHue);
            double fraction = sixHue - index;

            double steady = 1 - s;
            double falling = 1 - s * fraction;
            double rising = 1 - s * (1 - fraction);
            double maximum = 1;

            final double red, green, blue;

            switch (index) {
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


    private void updateHSV() {
        double r = _red, g = _green, b = _blue, a = _alpha;
        double h,s,v;

        double min = Math.min(Math.min(r,g),b);
        double max = Math.max(Math.max(r, g),b);
        v = max;

        if (max == 0) {
            h = s = 0;
        }
        else {
            double delta = max - min;
            s = delta / max;

            if (delta == 0) { h = 0; }
            else if (r == max) { h = (g-b)/delta / 6; }
            else if (g == max) { h = (2 + (b-r)/delta) / 6; }
            else { h = (4 + (r-g)/delta) / 6; }

            if (h < 0) { h += 1; }
        }

        _hue = h;
        _saturation = s;
        _value = v;
    }

    public double getRed() {
        return _red;
    }

    public double getGreen() {
        return _green;
    }

    public double getBlue() {
        return _blue;
    }

    public double getAlpha() {
        return _alpha;
    }

    public double getHue() {
        return _hue;
    }

    public double getSaturation() {
        return _saturation;
    }

    public double getValue() {
        return _value;
    }

    public void addListener(Listener l) {
        _listeners.add(l);
    }

    public void removeListener(Listener l) {
        _listeners.remove(l);
    }
}
