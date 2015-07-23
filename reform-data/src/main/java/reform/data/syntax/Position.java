package reform.data.syntax;

import java.util.Objects;

/**
 * Created by laszlokorte on 22.07.15.
 */
public class Position {
	public final int index;
	public final int lineNumber;
	public final int columnNumber;

	public Position(int index, int lineNumber, int columnNumber) {
		this.index = index;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public String toString() {
		return String.format("Pos(%d,l:%d,c:%d)", index, lineNumber, columnNumber);
	}

	public int hashCode() {
		return Objects.hash(index, lineNumber, columnNumber);
	}

	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		} else if(this == obj) {
			return true;
		} else if(obj.getClass() != getClass()) {
			return false;
		}

		Position other = (Position)obj;

		return other.index == index && other.lineNumber == lineNumber && other.columnNumber == columnNumber;
	}
}

