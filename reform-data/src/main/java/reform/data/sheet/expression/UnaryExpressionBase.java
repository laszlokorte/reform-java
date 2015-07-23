package reform.data.sheet.expression;


import java.util.Collection;

abstract class UnaryExpressionBase implements Expression
{
	final Expression _inner;

	UnaryExpressionBase(Expression inner) {
		_inner = inner;
	}

	@Override
	public void collectDependencies(Collection<ReferenceExpression> dependencies) {
		_inner.collectDependencies(dependencies);
	}

	public Expression getInner() {
		return _inner;
	}

}
