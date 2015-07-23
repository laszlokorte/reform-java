package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Solver;
import reform.data.sheet.Value;

import java.util.Collection;

/**
 * Created by laszlokorte on 21.07.15.
 */
public class ExponentialExpression extends BinaryExpressionBase implements Expression {

	public ExponentialExpression(Expression lhs, Expression rhs) {
		super(lhs, rhs);
	}

	@Override
	public String asString(boolean parens) {
		boolean leftParen = !(_lhs instanceof  ReferenceExpression) && !(_lhs instanceof  ConstantExpression);
		boolean rightParen = !(_rhs instanceof  ReferenceExpression) && !(_rhs instanceof  ConstantExpression);
		String op = " ^ ";
		String s = _lhs.asString(leftParen) + op + _rhs.asString(rightParen);
		if(parens) {
			return "("+s+")";
		} else {
			return s;
		}
	}
	@Override
	public Value getValueFor(DataSet set) {
		return Calculator.pow(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
