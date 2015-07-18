package reform.core.project;

import reform.identity.IdentifiableList;
import reform.identity.Identifier;
import reform.naming.Name;

public class DataSet {
	private final DataSet _parent;
	private final IdentifiableList<DataDefinition> _dataList = new IdentifiableList<>();

	public DataSet() {
		this(null);
	}

	public DataSet(final DataSet parent) {
		_parent = parent;
	}

	public void addData(final DataDefinition definition) {
		_dataList.add(definition);
	}

	public void removeData(final DataDefinition definition) {
		_dataList.remove(definition);
	}

	public void setNameForId(final Identifier<? extends DataDefinition> id,
			final Name name) {
		_dataList.getById(id).setName(name);
	}

	public void setValueForId(final Identifier<? extends DataDefinition> id,
			final DataValue value) {
		_dataList.getById(id).setValue(value);
	}

	public DataValue getValueForId(final Identifier<? extends DataDefinition> id) {
		if (_dataList.contains(id)) {
			return _dataList.getById(id).getValue();
		} else if (_parent != null) {
			return _parent.getValueForId(id);
		} else {
			return null;
		}
	}

	public int size() {
		final int parentSize = _parent != null ? _parent.size() : 0;
		int ownSize = 0;

		for (int i = 0, j = _dataList.size(); i < j; i++) {
			if (!_parent.hasValueForId(_dataList.getIdAtIndex(i))) {
				ownSize++;
			}
		}

		return parentSize + ownSize;
	}

	boolean hasValueForId(final Identifier<? extends DataDefinition> id) {
		return _dataList.contains(id) || _parent.hasValueForId(id);
	}

	boolean hasOwnValueForId(final Identifier<? extends DataDefinition> id) {
		return _dataList.contains(id);
	}

}
