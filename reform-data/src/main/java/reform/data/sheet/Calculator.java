package reform.data.sheet;

import java.util.regex.Pattern;

public final class Calculator
{
	public static class SemanticException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;

	}


	public enum Function
	{

		Int(1)
				{
					Value apply(Value val)
					{
						return new Value(val.getInteger());
					}
				},

		Float(1)
				{
					Value apply(Value val)
					{
						return new Value(val.getDouble());
					}
				},

		String(1)
				{
					Value apply(Value val)
					{
						return new Value(val.getString());
					}
				},

		Boolean(1)
				{
					Value apply(Value val)
					{
						return new Value(val.getBoolean());
					}
				},

		Sin(1)
				{
					Value apply(Value val)
					{
						return new Value(Math.sin(val.getDouble()));
					}
				}, Cos(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.cos(val.getDouble()));
				}
			}, Tan(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.tan(val.getDouble()));
				}
			},
		Asin(1)
				{
					Value apply(Value val)
					{
						return new Value(Math.asin(val.getDouble()));
					}
				}, Acos(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.acos(val.getDouble()));
				}
			}, Atan(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.atan(val.getDouble()));
				}
			}, Atan2(2)
			{
				Value apply(Value a, Value b)
				{
					return new Value(Math.atan2(a.getDouble(), b.getDouble()));
				}
			},
		Pow(2)
				{
					Value apply(Value a, Value b)
					{
						return new Value(Math.pow(a.getDouble(), b.getDouble()));
					}
				}, Sqrt(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.sqrt(val.getDouble()));
				}
			}, Exp(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.exp(val.getDouble()));
				}
			},
		Min
				{
					Value apply(Value... val)
					{
						double accum = Double.POSITIVE_INFINITY;
						boolean intOnly = true;
						for (int i = 0; i < val.length; i++)
						{
							intOnly &= val[i].type == Value.Type.Integer;
							accum = Math.min(accum, val[i].getDouble());
						}
						return intOnly ? new Value((int) accum) : new Value(accum);
					}
				}, Max
			{
				Value apply(Value... val)
				{
					double accum = Double.NEGATIVE_INFINITY;
					boolean intOnly = true;
					for (int i = 0; i < val.length; i++)
					{
						intOnly &= val[i].type == Value.Type.Integer;
						accum = Math.max(accum, val[i].getDouble());
					}

					return intOnly ? new Value((int) accum) : new Value(accum);
				}
			}, Avg
			{
				Value apply(Value... val)
				{
					double accum = 0;
					for (int i = 0; i < val.length; i++)
					{
						accum += val[i].getDouble();
					}
					return new Value(accum / val.length);
				}
			}, Count
			{
				Value apply(Value... val)
				{
					return new Value(val.length);
				}
			}, Sum
			{
				Value apply(Value... val)
				{
					double accum = 0;
					boolean intOnly = true;
					for (int i = 0; i < val.length; i++)
					{
						intOnly &= val[i].type == Value.Type.Integer;
						accum += val[i].getDouble();
					}
					return intOnly ? new Value((int) accum) : new Value(accum);
				}
			},
		Log(1)
				{
					Value apply(Value val)
					{
						return new Value(Math.log10(val.getDouble()));
					}
				}, Ln(1)
			{
				Value apply(Value val)
				{
					return new Value(Math.log(val.getDouble()));
				}
			},
		Floor(1)
				{
					Value apply(Value val)
					{
						return new Value((int) Math.sqrt(val.getDouble()));
					}
				}, Ceil(1)
			{
				Value apply(Value val)
				{
					return new Value((int) Math.ceil(val.getDouble()));
				}
			}, Round(1)
			{
				Value apply(Value val)
				{
					return new Value((int) Math.round(val.getDouble()));
				}
			},
		Abs(1)
				{
					Value apply(Value val)
					{
						return new Value(Math.abs(val.getDouble()));
					}
				},
		Random(0)
				{
					Value apply()
					{
						return new Value(Math.random());
					}
				};

		public final int arity;
		public final boolean variadic;

		private Function(int arity)
		{
			this.arity = arity;
			this.variadic = false;

		}

		private Function()
		{
			this.arity = 0;
			this.variadic = true;
		}

		Value apply(Value... val)
		{
			throw new RuntimeException("");
		}

		Value apply(Value val)
		{
			throw new RuntimeException("");
		}

		Value apply(Value a, Value b)
		{
			throw new RuntimeException("");
		}

		Value apply()
		{
			throw new RuntimeException("");
		}

	}

	private final static Pattern ZERO = Pattern.compile("0");

	public static Value modulo(final Value lhs, final Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(lhs.getDouble() % rhs.getDouble());
		}
		else
		{
			return new Value(lhs.getInteger() % rhs.getInteger());
		}
	}

	public static Value add(final Value lhs, final Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			return new Value(lhs.getString() + rhs.getString());
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(lhs.getDouble() + rhs.getDouble());
		}
		else
		{
			return new Value(lhs.getInteger() + rhs.getInteger());
		}
	}

	public static Value subtract(final Value lhs, final Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(lhs.getDouble() - rhs.getDouble());
		}
		else
		{
			return new Value(lhs.getInteger() - rhs.getInteger());
		}
	}

	public static Value divide(final Value lhs, final Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(lhs.getDouble() / rhs.getDouble());
		}
		else
		{
			return new Value(lhs.getInteger() / rhs.getInteger());
		}
	}

	public static Value multiply(final Value lhs, final Value rhs)
	{

		if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.String && rhs.type == Value.Type.Integer)
		{
			if (rhs.getInteger() < 1)
			{
				return new Value("");
			}
			return new Value(ZERO.matcher(String.format("%0" + rhs.getInteger() + "d", 0)).replaceAll(lhs.getString
					()));
		}
		else if (lhs.type == Value.Type.Integer && rhs.type == Value.Type.String)
		{
			if (lhs.getInteger() < 1)
			{
				return new Value("");
			}
			return new Value(ZERO.matcher(String.format("%0" + lhs.getInteger() + "d", 0)).replaceAll(rhs.getString
					()));
		}
		else if (lhs.type == Value.Type.String && rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(lhs.getDouble() * rhs.getDouble());
		}
		else
		{
			return new Value(lhs.getInteger() * rhs.getInteger());
		}
	}

	public static Value pow(Value lhs, Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Double || rhs.type == Value.Type.Double)
		{
			return new Value(Math.pow(lhs.getDouble(), rhs.getDouble()));
		}
		else
		{
			return new Value(Math.pow(lhs.getInteger(), rhs.getInteger()));
		}
	}

	public static Value negate(Value op)
	{
		if (op.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (op.type == Value.Type.Double)
		{
			return new Value(-op.getDouble());
		}
		else
		{
			return new Value(-op.getInteger());
		}
	}

	public static Value logicNegate(Value op)
	{
		return new Value(!op.getBoolean());
	}

	public static Value logicAnd(Value a, Value b)
	{
		return new Value(a.getBoolean() && b.getBoolean());
	}

	public static Value logicOr(Value a, Value b)
	{
		return new Value(a.getBoolean() || b.getBoolean());
	}

	public static Value greaterThan(Value a, Value b)
	{
		if(a.type != Value.Type.Integer && a.type != Value.Type.Double) {
			throw new SemanticException();
		}
		if(b.type != Value.Type.Integer && b.type != Value.Type.Double) {
			throw new SemanticException();
		}

		return new Value(a.getDouble() > b.getDouble());
	}

	public static Value lessThan(Value a, Value b)
	{
		if(a.type != Value.Type.Integer && a.type != Value.Type.Double) {
			throw new SemanticException();
		}
		if(b.type != Value.Type.Integer && b.type != Value.Type.Double) {
			throw new SemanticException();
		}

		return new Value(a.getDouble() < b.getDouble());
	}

	public static Value greaterThanEqual(Value a, Value b)
	{
		if(a.type != Value.Type.Integer && a.type != Value.Type.Double) {
			throw new SemanticException();
		}
		if(b.type != Value.Type.Integer && b.type != Value.Type.Double) {
			throw new SemanticException();
		}

		return new Value(a.getDouble() >= b.getDouble());
	}

	public static Value lessThanEqual(Value a, Value b)
	{
		if(a.type != Value.Type.Integer && a.type != Value.Type.Double) {
			throw new SemanticException();
		}
		if(b.type != Value.Type.Integer && b.type != Value.Type.Double) {
			throw new SemanticException();
		}

		return new Value(a.getDouble() <= b.getDouble());
	}

	public static Value strictEqual(Value a, Value b)
	{
		if(a.type != b.type) {
			new Value(false);
		}

		switch (a.type) {
			case String: return new Value(a.getString().equals(b.getString()));
			case Integer:  return new Value(a.getInteger() == b.getInteger());
			case Double:  return new Value(a.getDouble() == b.getDouble());
			case Boolean:  return new Value(a.getBoolean() == b.getBoolean());
			default: return new Value(false);
		}
	}

	public static Value apply(Function func, Value[] params)
	{
		if (!func.variadic && params.length != func.arity)
		{
			throw new RuntimeException("");
		}
		if (func.variadic)
		{
			if (params.length < 1)
			{
				throw new RuntimeException("");
			}
			return func.apply(params);
		}
		else if (func.arity == 1)
		{
			return func.apply(params[0]);
		}
		else if (func.arity == 2)
		{
			return func.apply(params[0], params[1]);
		}
		else if (func.arity == 0)
		{
			return func.apply();
		}
		else
		{
			throw new RuntimeException("");
		}
	}
}
