package reform.data.expressions;

import reform.data.Calculator;
import reform.data.CycleException;
import reform.data.Expression;
import reform.data.ExpressionContext;
import reform.data.SemanticException;
import reform.data.Value;

public class DivisionExpression implements Expression {
	private final Expression _leftHandSide;
	private final Expression _rightHandSide;

	public DivisionExpression(final Expression lhs, final Expression rhs) {
		_leftHandSide = lhs;
		_rightHandSide = rhs;
	}

	@Override
	public Value evaluate(final ExpressionContext context)
			throws CycleException, SemanticException {
		return Calculator.divide(_leftHandSide.evaluate(context),
                _rightHandSide.evaluate(context));
	}

}