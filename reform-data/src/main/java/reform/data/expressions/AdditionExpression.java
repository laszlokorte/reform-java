package reform.data.expressions;

import reform.data.*;

public class AdditionExpression implements Expression
{
	private final Expression _leftHandSide;
	private final Expression _rightHandSide;

	public AdditionExpression(final Expression lhs, final Expression rhs)
	{
		_leftHandSide = lhs;
		_rightHandSide = rhs;
	}

	@Override
	public Value evaluate(final ExpressionContext context) throws CycleException, SemanticException
	{
		return Calculator.add(_leftHandSide.evaluate(context), _rightHandSide.evaluate(context));
	}

}
