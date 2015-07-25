package reform.data.syntax;


import java.util.Objects;

public class Token
{
	public enum Type
	{
		LiteralValue,
		ArgumentSeparator,
		Identifier, ParenthesisLeft, ParenthesisRight, Operator,
		EOF, Ignore, FunctionName
	}

	public final Type type;
	public final CharSequence value;
	public final Position position;

	public Token(final Type type, final CharSequence value, final Position position)
	{
		this.type = type;
		this.value = value;
		this.position = position;
	}

	public int hashCode()
	{
		return Objects.hash(type, value, position);
	}

	public boolean equals(final Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		else if (this == obj)
		{
			return true;
		}
		else if (obj.getClass() != getClass())
		{
			return false;
		}

		final Token other = (Token) obj;

		return other.type.equals(type) && other.value.equals(value) && other.position.equals(position);
	}

	public String toString()
	{
		return String.format("%s: %s %s", type.name(), value, position);
	}
}
