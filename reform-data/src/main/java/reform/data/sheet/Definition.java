package reform.data.sheet;

import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.ReferenceExpression;
import reform.identity.Identifier;

import java.util.HashSet;
import java.util.Set;

public class Definition
{
	private final Identifier<? extends Definition> _id;
	private final HashSet<ReferenceExpression> _dependencies = new HashSet<>();
	boolean _fresh = false;
	int _incoming = 0;
	private String _name;
	private Expression _expression;

	public Definition(final Identifier<? extends Definition> id, final String name,
	                  final Expression expression)
	{
		_id = id;
		_expression = expression;
		_name = name;
	}

	public Identifier<? extends Definition> getId()
	{
		return _id;
	}

	public Expression getExpression()
	{
		return _expression;
	}

	public void setExpression(final Expression expression)
	{
		_fresh = false;
		_expression = expression;
	}

	boolean refreshDependencies()
	{
		if (!_fresh)
		{
			_dependencies.clear();
			_expression.collectDependencies(_dependencies);
			_fresh = true;
			return false;
		}

		return true;
	}

	Set<ReferenceExpression> getDependencies()
	{
		return _dependencies;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(final String name)
	{
		_name = name;
	}
}
