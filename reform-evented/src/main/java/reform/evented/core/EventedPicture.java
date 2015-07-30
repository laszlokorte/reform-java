package reform.evented.core;

import reform.core.procedure.Procedure;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.data.sheet.Sheet;
import reform.identity.Identifier;
import reform.math.Vec2i;
import reform.naming.Name;

import java.util.ArrayList;

public class EventedPicture
{
	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final EventedProject _evtProject;
	private final Identifier<? extends Picture> _pictureId;

	public EventedPicture(final EventedProject evtProject, final Identifier<? extends
			Picture> pictureId)
	{
		_evtProject = evtProject;
		_pictureId = pictureId;
	}

	public Name getName()
	{
		return _evtProject.getPicture(_pictureId).getName();
	}

	public void setName(final Name name)
	{
		_evtProject.getPicture(_pictureId).setName(name);
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onNameChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public Vec2i getSize()
	{
		return _evtProject.getPicture(_pictureId).getSize();
	}

	public void setSize(final Vec2i size)
	{
		if (size.x < 1 || size.y < 1)
		{
			throw new IllegalArgumentException("Size must be > 0");
		}
		_evtProject.getPicture(_pictureId).setSize(size);
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onSizeChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public EventedProcedure getEventedProcedure()
	{
		return new EventedProcedure(this);
	}

	Procedure getProcedure()
	{
		return _evtProject.getPicture(_pictureId).getProcedure();
	}

	public EventedSheet getEventedDataSheet()
	{
		return new EventedDataSheet(this);
	}

	public EventedSheet getEventedMeasurementSheet()
	{
		return new EventedMeasurementSheet(this);
	}

	Sheet getDataSheet()
	{
		return _evtProject.getPicture(_pictureId).getDataSheet();
	}

	Sheet getMeasurementSheet()
	{
		return _evtProject.getPicture(_pictureId).getMeasurementSheet();
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	void propagateProcedureChange()
	{
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onProcedureChanged(this);
		}
		_evtProject.propagatePictureChange(_pictureId);
	}

	public Project getProject()
	{
		return _evtProject.getRaw();
	}

	public EventedProject getEventedProject()
	{
		return _evtProject;
	}

	public boolean exists()
	{
		return _evtProject.containsPicture(_pictureId);
	}

	public Identifier<? extends Picture> getId()
	{
		return _pictureId;
	}

	public Identifier<? extends Picture> getNearestPicture()
	{
		int ownIndex = _evtProject.getIndexOf(_pictureId);
		int count = _evtProject.getPictureCount();

		if(ownIndex+1 < count) {
			return _evtProject.getPictureAtIndex(ownIndex+1);
		} else if(ownIndex > 0) {
			return _evtProject.getPictureAtIndex(ownIndex-1);
		} else {
			return _pictureId;
		}
	}

	public interface Listener
	{
		void onNameChanged(EventedPicture picture);

		void onSizeChanged(EventedPicture picture);

		void onProcedureChanged(EventedPicture picture);
	}
}
