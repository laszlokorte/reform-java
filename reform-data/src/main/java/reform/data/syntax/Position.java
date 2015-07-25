package reform.data.syntax;

import java.util.Objects;

public class Position
{
	public final int index;
	public final int lineNumber;
	public final int columnNumber;

	public Position(final int index, final int lineNumber, final int columnNumber)
	{
		this.index = index;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public String toString()
	{
		return String.format("Pos(%d,l:%d,c:%d)", index, lineNumber, columnNumber);
	}

	public int hashCode()
	{
		return Objects.hash(index, lineNumber, columnNumber);
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

		final Position other = (Position) obj;

		return other.index == index && other.lineNumber == lineNumber && other.columnNumber == columnNumber;
	}
}

