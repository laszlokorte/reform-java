package reform.data.sheet;

import com.sun.org.apache.xerces.internal.xni.XMLString;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.ReferenceExpression;
import reform.identity.Identifier;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class Definition {
	private final Identifier<? extends Definition> _id;
	private String _name;
	private Expression _expression;
	private final HashSet<ReferenceExpression> _dependencies = new HashSet<>();
	boolean _fresh = false;
	int _incoming = 0;

	public Definition(Identifier<? extends Definition> id, String name, Expression expression) {
		_id = id;
		_expression = expression;
		_name = name;
	}

	public Identifier<? extends Definition> getId() {
		return _id;
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		_fresh = false;
		_expression = expression;
	}

	boolean refreshDependencies() {
		if(!_fresh) {
			_dependencies.clear();
			_expression.collectDependencies(_dependencies);
			_fresh = true;
			return false;
		}

		return true;
	}

	Set<ReferenceExpression> getDependencies() {
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
