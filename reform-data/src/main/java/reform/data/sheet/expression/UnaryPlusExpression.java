package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.Value;
import reform.identity.Identifier;

import java.util.Collection;

/**
 * Created by laszlokorte on 22.07.15.
 */
public final class UnaryPlusExpression extends UnaryExpressionBase implements Expression {

	public UnaryPlusExpression(Expression inner) {
		super(inner);
	}


	@Override
	public String asString(boolean parens) {
		String s =  "+" + _inner.asString(!(_inner instanceof ConstantExpression || _inner instanceof ReferenceExpression || _inner instanceof ExponentialExpression));
		if(parens) {
			return "("+s+")";
		} else {
			return s;
		}
	}
	@Override
	public Value getValueFor(DataSet set) {
		return _inner.getValueFor(set);
	}

}
