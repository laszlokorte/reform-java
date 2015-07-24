package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

/**
 * Created by laszlokorte on 21.07.15.
 */
public final class EqualExpression extends BinaryExpressionBase implements Expression {
	public EqualExpression(Expression lhs, Expression rhs) {
		super(lhs, rhs);
	}

	@Override
	public String asString(boolean parens) {
		String s = _lhs.asString(false) + " == " + _rhs.asString(false);
		if(parens) {
			return "("+s+")";
		} else {
			return s;
		}
	}

	@Override
	public Value getValueFor(DataSet set) {
		return Calculator.strictEqual(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
