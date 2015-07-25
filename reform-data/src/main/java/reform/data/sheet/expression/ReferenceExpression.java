package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Value;
import reform.identity.Identifier;

import java.util.Collection;

public class ReferenceExpression implements Expression
{

	private final Identifier<? extends Definition> _id;
	private String _label;

	public ReferenceExpression(final Identifier<? extends Definition> id, final String label)
	{
		_id = id;
		_label = label;
	}

	@Override
	public String asString(final boolean paren)
	{
		return _label;
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		return set.lookUp(_id);
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{
		dependencies.add(this);
	}

	public Identifier<? extends Definition> getId()
	{
		return _id;
	}

	public void setLabel(final String label)
	{
		_label = label;
	}
}
