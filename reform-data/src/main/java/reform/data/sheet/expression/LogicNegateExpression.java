package reform.data.sheet.expression;

import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

/**
 * Created by laszlokorte on 22.07.15.
 */
public final class LogicNegateExpression extends  UnaryExpressionBase implements Expression {

	public LogicNegateExpression(Expression inner) {
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
		return Calculator.logicNegate(_inner.getValueFor(set));
	}

	public static Expression wrapOrSimplify(Expression operand) {
		if (operand instanceof LogicNegateExpression) {
			return ((LogicNegateExpression) operand)._inner;
		} else if (operand instanceof ConstantExpression) {
			ConstantExpression e = (ConstantExpression) operand;
			Value v = e.getValue();
			if (v.type == Value.Type.Boolean) {
				return new ConstantExpression(Calculator.logicNegate(v));
			}
		}
		return new LogicNegateExpression(operand);

	}
}
