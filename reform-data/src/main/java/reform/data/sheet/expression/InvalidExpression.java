package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

/**
 * Created by laszlokorte on 21.07.15.
 */
public final class InvalidExpression implements Expression {
	private final static Value NULL = new Value(0);

	private final String _expressionString;

	public InvalidExpression(String string) {
		_expressionString = string;
	}

	@Override
	public String asString(boolean parens) {
		if(parens) {
			return "("+_expressionString+")";
		} else {
			return _expressionString;
		}
	}

	@Override
	public Value getValueFor(DataSet set) {
		return NULL;
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{

	}
}
