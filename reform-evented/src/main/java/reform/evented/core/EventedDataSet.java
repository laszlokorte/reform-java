package reform.evented.core;

import java.util.ArrayList;

import reform.core.project.DataDefinition;
import reform.core.project.DataValue;
import reform.identity.Identifier;
import reform.naming.Name;

public class EventedDataSet {
	public interface Listener {
		void onNameChanged(EventedDataSet picture,
                           Identifier<? extends DataDefinition> dataDefinition);

		void onValueChanged(EventedDataSet picture,
                            Identifier<? extends DataDefinition> dataDefinition);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final EventedPicture _evtPicture;

	public EventedDataSet(final EventedPicture evtPicture) {
		_evtPicture = evtPicture;
	}

	public void addListener(final Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener) {
		_listeners.remove(listener);
	}

	public void setNameForId(final Identifier<? extends DataDefinition> id,
                             final Name name) {
		_evtPicture.getDataSet().setNameForId(id, name);
	}

	public void setValueForId(final Identifier<? extends DataDefinition> id,
			final DataValue value) {
		_evtPicture.getDataSet().setValueForId(id, value);
	}

	public DataValue getValueForId(final Identifier<? extends DataDefinition> id) {
		return _evtPicture.getDataSet().getValueForId(id);
	}
}
