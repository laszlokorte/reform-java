package reform.data;

import reform.identity.Identifier;

public interface ExpressionContext
{
	Expression lookUp(Identifier<?> definitionId);

	ExpressionContext getScope(Identifier<?> objectId);

	boolean pushExpression(Expression expression);

	void popExpression(Expression expression);
}
