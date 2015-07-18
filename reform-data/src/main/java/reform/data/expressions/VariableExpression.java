package reform.data.expressions;

import reform.data.CycleException;
import reform.data.Expression;
import reform.data.ExpressionContext;
import reform.data.SemanticException;
import reform.data.Value;
import reform.identity.Identifier;

public class VariableExpression implements Expression {

	private final Identifier<?> _variableId;

	public VariableExpression(final Identifier<?> variableId) {
		_variableId = variableId;
	}

	@Override
	public Value evaluate(final ExpressionContext context)
			throws CycleException, SemanticException {
		if (!context.pushExpression(this)) {
			throw new CycleException();
		}
		try {
			return context.lookUp(_variableId).evaluate(context);
		} catch (final CycleException e) {
			return new Value();
		} finally {
			context.popExpression(this);
		}
	}
}