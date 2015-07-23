package reform.data.sheet.expression;


import reform.data.sheet.Definition;
import reform.identity.Identifier;

import java.sql.Ref;
import java.util.Collection;

abstract class BinaryExpressionBase implements Expression
{
	final Expression _lhs;
	final Expression _rhs;

	BinaryExpressionBase(Expression lhs, Expression rhs) {
		_lhs = lhs;
		_rhs = rhs;
	}

	@Override
	public void collectDependencies(Collection<ReferenceExpression> dependencies) {
		_lhs.collectDependencies(dependencies);
		_rhs.collectDependencies(dependencies);
	}

	public Expression getLeft() {
		return _lhs;
	}

	public Expression getRight() {
		return _rhs;
	}
}
