package reform.data.expressions;

import reform.data.*;
import reform.identity.Identifier;

public class PropertyExpression implements Expression
{

	private final Identifier<?> _objectId;
	private final Identifier<?> _propertyId;

	public PropertyExpression(final Identifier<?> objectId, final Identifier<?> propertyId)
	{
		_objectId = objectId;
		_propertyId = propertyId;
	}

	@Override
	public Value evaluate(final ExpressionContext context) throws CycleException, SemanticException
	{
		if (!context.pushExpression(this))
		{
			throw new CycleException();
		}
		try
		{
			final ExpressionContext subContext = context.getScope(_objectId);
			return subContext.lookUp(_propertyId).evaluate(context);
		} catch (final CycleException e)
		{
			return new Value();
		} finally
		{
			context.popExpression(this);
		}
	}
}
