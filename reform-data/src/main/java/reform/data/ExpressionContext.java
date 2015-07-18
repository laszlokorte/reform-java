package reform.data;

import reform.identity.Identifier;

public interface ExpressionContext {
	public Expression lookUp(Identifier<?> definitionId);

	public ExpressionContext getScope(Identifier<?> objectId);

	public boolean pushExpression(Expression expression);

	public void popExpression(Expression expression);
}
