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
					Value apply(final Value val)
					{
						return new Value(val.getInteger());
					}
				},

		Float(1)
				{
					Value apply(final Value val)
					{
						return new Value(val.getDouble());
					}
				},

		String(1)
				{
					Value apply(final Value val)
					{
						return new Value(val.getString());
					}
				},

		Boolean(1)
				{
					Value apply(final Value val)
					{
						return new Value(val.getBoolean());
					}
				},

		Rgb(3)
				{
					Value apply(final Value r, final Value g, final Value b)
					{
						return new Value(r.getDouble(), g.getDouble(), b.getDouble(), 1);
					}
				},

		Argb(4)
				{
					Value apply(final Value a, final Value r, final Value g, final Value b)
					{
						return new Value(r.getDouble(), g.getDouble(), b.getDouble(), a.getDouble());
					}
				},

		Sin(1)
				{
					Value apply(final Value val)
					{
						return new Value(Math.sin(val.getDouble()));
					}
				}, Cos(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.cos(val.getDouble()));
				}
			}, Tan(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.tan(val.getDouble()));
				}
			},
		Asin(1)
				{
					Value apply(final Value val)
					{
						return new Value(Math.asin(val.getDouble()));
					}
				}, Acos(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.acos(val.getDouble()));
				}
			}, Atan(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.atan(val.getDouble()));
				}
			}, Atan2(2)
			{
				Value apply(final Value a, final Value b)
				{
					return new Value(Math.atan2(a.getDouble(), b.getDouble()));
				}
			},
		Pow(2)
				{
					Value apply(final Value a, final Value b)
					{
						return new Value(Math.pow(a.getDouble(), b.getDouble()));
					}
				}, Sqrt(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.sqrt(val.getDouble()));
				}
			}, Exp(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.exp(val.getDouble()));
				}
			},
		Min
				{
					Value apply(final Value... val)
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
				Value apply(final Value... val)
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
				Value apply(final Value... val)
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
				Value apply(final Value... val)
				{
					return new Value(val.length);
				}
			}, Sum
			{
				Value apply(final Value... val)
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
					Value apply(final Value val)
					{
						return new Value(Math.log10(val.getDouble()));
					}
				}, Ln(1)
			{
				Value apply(final Value val)
				{
					return new Value(Math.log(val.getDouble()));
				}
			},
		Floor(1)
				{
					Value apply(final Value val)
					{
						return new Value((int) Math.sqrt(val.getDouble()));
					}
				}, Ceil(1)
			{
				Value apply(final Value val)
				{
					return new Value((int) Math.ceil(val.getDouble()));
				}
			}, Round(1)
			{
				Value apply(final Value val)
				{
					return new Value((int) Math.round(val.getDouble()));
				}
			},
		Abs(1)
				{
					Value apply(final Value val)
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

		Function(final int arity)
		{
			this.arity = arity;
			this.variadic = false;

		}

		Function()
		{
			this.arity = 0;
			this.variadic = true;
		}

		Value apply(final Value[] val)
		{
			throw new SemanticException();
		}

		Value apply(final Value val)
		{
			throw new SemanticException();
		}

		Value apply(final Value a, final Value b)
		{
			throw new SemanticException();
		}

		Value apply(final Value a, final Value b, final Value c)
		{
			throw new SemanticException();
		}

		Value apply(final Value a, final Value b, final Value c, final Value d)
		{
			throw new SemanticException();
		}


		Value apply()
		{
			throw new SemanticException();
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
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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

	public static Value pow(final Value lhs, final Value rhs)
	{
		if (lhs.type == Value.Type.String || rhs.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Boolean || rhs.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else if (lhs.type == Value.Type.Color || rhs.type == Value.Type.Color)
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

	public static Value negate(final Value op)
	{
		if (op.type == Value.Type.String)
		{
			throw new SemanticException();
		}
		else if (op.type == Value.Type.Double)
		{
			return new Value(-op.getDouble());
		}
		else if (op.type == Value.Type.Color)
		{
			throw new SemanticException();
		}
		else if (op.type == Value.Type.Boolean)
		{
			throw new SemanticException();
		}
		else
		{
			return new Value(-op.getInteger());
		}
	}

	public static Value logicNegate(final Value op)
	{
		return new Value(!op.getBoolean());
	}

	public static Value logicAnd(final Value a, final Value b)
	{
		return new Value(a.getBoolean() && b.getBoolean());
	}

	public static Value logicOr(final Value a, final Value b)
	{
		return new Value(a.getBoolean() || b.getBoolean());
	}

	public static Value greaterThan(final Value a, final Value b)
	{
		if (a.type != Value.Type.Integer && a.type != Value.Type.Double)
		{
			throw new SemanticException();
		}
		if (b.type != Value.Type.Integer && b.type != Value.Type.Double)
		{
			throw new SemanticException();
		}

		return new Value(a.getDouble() > b.getDouble());
	}

	public static Value lessThan(final Value a, final Value b)
	{
		if (a.type != Value.Type.Integer && a.type != Value.Type.Double)
		{
			throw new SemanticException();
		}
		if (b.type != Value.Type.Integer && b.type != Value.Type.Double)
		{
			throw new SemanticException();
		}
		return new Value(a.getDouble() < b.getDouble());
	}

	public static Value greaterThanEqual(final Value a, final Value b)
	{
		if (a.type != Value.Type.Integer && a.type != Value.Type.Double)
		{
			throw new SemanticException();
		}
		if (b.type != Value.Type.Integer && b.type != Value.Type.Double)
		{
			throw new SemanticException();
		}

		return new Value(a.getDouble() >= b.getDouble());
	}

	public static Value lessThanEqual(final Value a, final Value b)
	{
		if (a.type != Value.Type.Integer && a.type != Value.Type.Double)
		{
			throw new SemanticException();
		}
		if (b.type != Value.Type.Integer && b.type != Value.Type.Double)
		{
			throw new SemanticException();
		}

		return new Value(a.getDouble() <= b.getDouble());
	}

	public static Value strictEqual(final Value a, final Value b)
	{
		if (a.type != b.type)
		{
			new Value(false);
		}

		switch (a.type)
		{
			case String:
				return new Value(a.getString().equals(b.getString()));
			case Integer:
				return new Value(a.getInteger() == b.getInteger());
			case Double:
				return new Value(a.getDouble() == b.getDouble());
			case Boolean:
				return new Value(a.getBoolean() == b.getBoolean());
			case Color:
				return new Value(a.getColor() == b.getColor());
			default:
				return new Value(false);
		}
	}

	public static Value apply(final Function func, final Value[] params)
	{
		if (!func.variadic && params.length != func.arity)
		{
			throw new SemanticException();
		}
		if (func.variadic)
		{
			if (params.length < 1)
			{
				throw new SemanticException();
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
		else if (func.arity == 3)
		{
			return func.apply(params[0], params[1], params[2]);
		}
		else if (func.arity == 4)
		{
			return func.apply(params[0], params[1], params[2], params[3]);
		}
		else if (func.arity == 0)
		{
			return func.apply();
		}
		else
		{
			throw new SemanticException();
		}
	}
}
