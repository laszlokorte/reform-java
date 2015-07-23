package reform.data.sheet.expression;


import reform.data.sheet.*;
import reform.identity.Identifier;

import java.util.Collection;

/**
 * Created by laszlokorte on 21.07.15.
 */
public class SubtractionExpression extends  BinaryExpressionBase  implements Expression {

	public SubtractionExpression(Expression lhs, Expression rhs) {
		super(lhs, rhs);
	}

	@Override
	public String asString(boolean parens) {
		String s = _lhs.asString(false) + " - " + _rhs.asString(false);
		if(parens) {
			return "("+s+")";
		} else {
			return s;
		}
	}
	@Override
	public Value getValueFor(DataSet set) {
		return Calculator.subtract(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
