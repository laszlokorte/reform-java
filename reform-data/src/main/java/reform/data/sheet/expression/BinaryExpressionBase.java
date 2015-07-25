package reform.data.sheet.expression;


import java.util.Collection;

abstract class BinaryExpressionBase implements Expression
{
	final Expression _lhs;
	final Expression _rhs;

	BinaryExpressionBase(final Expression lhs, final Expression rhs)
	{
		_lhs = lhs;
		_rhs = rhs;
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{
		_lhs.collectDependencies(dependencies);
		_rhs.collectDependencies(dependencies);
	}

	public Expression getLeft()
	{
		return _lhs;
	}

	public Expression getRight()
	{
		return _rhs;
	}
}
