package reform.core.project;

import reform.core.procedure.Procedure;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.naming.Name;

public final class Picture implements Identifiable<Picture> {

	private final Identifier<Picture> _id;

	private Name _name;
	private final Vec2i _size = new Vec2i(400, 400);

	private final DataSet _dataSet;
	private final Procedure _procedure;

	public Picture(final Identifier<Picture> id, final Name name,
			final Vec2i size, final DataSet dataSet, final Procedure procedure) {
		_id = id;
		_name = name;
		_size.set(size);
		_dataSet = dataSet;
		_procedure = procedure;
	}

	public void setName(final Name name) {
		_name = name;
	}

	public Name getName() {
		return _name;
	}

	public void setSize(final Vec2i size) {
		_size.set(size);
	}

	public Vec2i getSize() {
		return _size;
	}

	public DataSet getDataSet() {
		return _dataSet;
	}

	public Procedure getProcedure() {
		return _procedure;
	}

	@Override
	public Identifier<Picture> getId() {
		return _id;
	}

}
