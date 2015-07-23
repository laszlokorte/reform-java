package reform.data.sheet.expression;

import reform.data.sheet.*;
import reform.identity.Identifier;

import java.util.Collection;

/**
 * Created by laszlokorte on 22.07.15.
 */
public final class UnaryMinusExpression extends  UnaryExpressionBase implements Expression {

	public UnaryMinusExpression(Expression inner) {
		super(inner);
	}


	@Override
	public String asString(boolean parens) {
		String s = "-" + _inner.asString(!(_inner instanceof ConstantExpression || _inner instanceof ReferenceExpression ||
				_inner instanceof ExponentialExpression));
		if (parens) {
			return "(" + s + ")";
		} else {
			return s;
		}
	}

	@Override
	public Value getValueFor(DataSet set) {
		return Calculator.negate(_inner.getValueFor(set));
	}

	public static Expression wrapOrSimplify(Expression operand) {
		if (operand instanceof UnaryMinusExpression) {
			return ((UnaryMinusExpression) operand)._inner;
		} else if (operand instanceof ConstantExpression) {
			ConstantExpression e = (ConstantExpression) operand;
			Value v = e.getValue();
			if (v.type == Value.Type.Integer || v.type == Value.Type.Double) {
				return new ConstantExpression(Calculator.negate(v));
			}
		}
		return new UnaryMinusExpression(operand);

	}
}
