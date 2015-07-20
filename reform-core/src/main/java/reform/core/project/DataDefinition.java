package reform.core.project;

import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.naming.Name;

public class DataDefinition implements Identifiable<DataDefinition>
{

	private final Identifier<DataDefinition> _id;
	private Name _name;
	private DataValue _value;

	public DataDefinition(final Identifier<DataDefinition> id, final Name name, final DataValue value)
	{
		_id = id;
		_name = name;
		_value = value;
	}

	@Override
	public Identifier<DataDefinition> getId()
	{
		return _id;
	}

	public Name getName()
	{
		return _name;
	}

	public void setName(final Name name)
	{
		_name = name;
	}

	public DataValue getValue()
	{
		return _value;
	}

	public void setValue(final DataValue value)
	{
		_value = value;
	}

}
