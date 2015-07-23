package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Solver;
import reform.data.sheet.Value;

import java.util.Collection;

/**
 * Created by laszlokorte on 21.07.15.
 */
public class DivisionExpression extends BinaryExpressionBase implements Expression {
	public DivisionExpression(Expression lhs, Expression rhs) {
		super(lhs, rhs);
	}

	@Override
	public String asString(boolean parens) {
		boolean leftParen = _lhs instanceof  AdditionExpression || _lhs instanceof SubtractionExpression;
		boolean rightParen = _rhs instanceof  AdditionExpression || _rhs instanceof SubtractionExpression;
		String op = " / ";
		String s = _lhs.asString(leftParen) + op + _rhs.asString(rightParen);
		if(parens) {
			return "("+s+")";
		} else {
			return s;
		}
	}
	@Override
	public Value getValueFor(DataSet set) {
		return Calculator.divide(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
