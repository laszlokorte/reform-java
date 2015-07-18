package reform.evented.core;

import java.util.ArrayList;

import reform.core.procedure.Procedure;
import reform.core.project.DataSet;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.naming.Name;

public class EventedPicture {
	public static interface Listener {
		public void onNameChanged(EventedPicture picture);

		public void onSizeChanged(EventedPicture picture);

		public void onProcedureChanged(EventedPicture picture);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final EventedProject _evtProject;
	private final Identifier<? extends Picture> _pictureId;

	public EventedPicture(final EventedProject evtProject,
			final Identifier<? extends Picture> pictureId) {
		_evtProject = evtProject;
		_pictureId = pictureId;
	}

	public void setName(final Name name) {
		_evtProject.getPicture(_pictureId).setName(name);
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onNameChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public Name getName() {
		return _evtProject.getPicture(_pictureId).getName();
	}

	public void setSize(final Vec2i size) {
		if (size.x < 1 || size.y < 1) {
			throw new IllegalArgumentException("Size must be > 0");
		}
		_evtProject.getPicture(_pictureId).setSize(size);
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onSizeChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public Vec2i getSize() {
		return _evtProject.getPicture(_pictureId).getSize();
	}

	public EventedProcedure getEventedProcedure() {
		return new EventedProcedure(this);
	}

	Procedure getProcedure() {
		return _evtProject.getPicture(_pictureId).getProcedure();
	}

	public EventedDataSet getEventedDataSet() {
		return new EventedDataSet(this);
	}

	DataSet getDataSet() {
		return _evtProject.getPicture(_pictureId).getDataSet();
	}

	public void addListener(final Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener) {
		_listeners.remove(listener);
	}

	void propagateProcedureChange() {
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onProcedureChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public Project getProject() {
		return _evtProject.getRaw();
	}

	public boolean exists() {
		return _evtProject.containsPicture(_pictureId);
	}
}
