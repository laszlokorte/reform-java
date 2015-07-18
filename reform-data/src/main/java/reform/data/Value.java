package reform.data;

public class Value {
	public static enum Type {
		String, Intenger, Double;
	}

	private final Type _type;
	private final int _integer;
	private final double _double;
	private final String _string;

	public Value() {
		this(0);
	}

	public Value(final int integer) {
		_type = Type.Intenger;
		_integer = integer;
		_double = integer;
		_string = integer + "";
	}

	public Value(final double doub) {
		_type = Type.Double;
		_integer = (int) doub;
		_double = doub;
		_string = String.format("%f.2", doub);
	}

	public Value(final String string) {
		_type = Type.String;
		_integer = Integer.parseInt(string);
		_double = Double.parseDouble(string);
		_string = string;
	}

	public int getInteger() {
		return _integer;
	}

	public double getDouble() {
		return _double;
	}

	public String getString() {
		return _string;
	}

	public Type getType() {
		return _type;
	}
}