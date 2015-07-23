package reform.core.project;

import reform.core.procedure.Procedure;
import reform.data.sheet.Sheet;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.naming.Name;

public final class Picture implements Identifiable<Picture>
{

	private final Identifier<? extends Picture> _id;

	private Name _name;
	private final Vec2i _size = new Vec2i(400, 400);

	private final Sheet _dataSheet;
	private final Procedure _procedure;
	private final Sheet _measurements;

	public Picture(final Identifier<? extends Picture> id, final Name name, final Vec2i size, final Sheet dataSheet,
	               final Procedure procedure, Sheet measurements)
	{
		_id = id;
		_name = name;
		_size.set(size);
		_dataSheet = dataSheet;
		_procedure = procedure;
		_measurements = measurements;
	}

	public void setName(final Name name)
	{
		_name = name;
	}

	public Name getName()
	{
		return _name;
	}

	public void setSize(final Vec2i size)
	{
		_size.set(size);
	}

	public Vec2i getSize()
	{
		return _size;
	}

	public Sheet getDataSheet()
	{
		return _dataSheet;
	}

	public Procedure getProcedure()
	{
		return _procedure;
	}

	public Sheet getMeasurementSheet()
	{
		return _measurements;
	}

	@Override
	public Identifier<? extends Picture> getId()
	{
		return _id;
	}

}
