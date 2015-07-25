package reform.evented.core;

import reform.core.project.Picture;
import reform.core.project.Project;
import reform.identity.FastIterable;
import reform.identity.Identifier;

import java.util.ArrayList;

public class EventedProject
{

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final Project _project;

	public EventedProject(final Project project)
	{
		_project = project;
	}

	public void addPicture(final Picture picture)
	{
		_project.addPicture(picture);
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onPictureAdded(this, picture.getId());
		}
	}

	public void removePicture(final Identifier<? extends Picture> pictureId)
	{
		_project.removePicture(pictureId);
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onPictureRemoved(this, pictureId);
		}
	}

	public FastIterable<Identifier<? extends Picture>> getPictures()
	{
		return _project.getPictures();
	}

	public EventedPicture getEventedPicture(final Identifier<? extends Picture>
			                                        pictureId)
	{
		return new EventedPicture(this, pictureId);
	}

	Picture getPicture(final Identifier<? extends Picture> pictureId)
	{
		return _project.getPicture(pictureId);
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	void propagatePictureChange(final Identifier<? extends Picture> pictureId)
	{
		for (int i = 0, j = _listeners.size(); i < j; i++)
		{
			_listeners.get(i).onPictureChanged(this, pictureId);
		}
	}

	public Project getRaw()
	{
		return _project;
	}

	public Identifier<? extends Picture> getPictureAtIndex(final int index)
	{
		return _project.getPictureAtIndex(index);
	}

	boolean containsPicture(final Identifier<? extends Picture> pictureId)
	{
		return _project.containsPicture(pictureId);
	}

	public int getPictureCount()
	{
		return _project.getPictureCount();
	}

	public interface Listener
	{
		void onPictureAdded(EventedProject project, Identifier<? extends Picture>
				pictureId);

		void onPictureRemoved(EventedProject project, Identifier<? extends Picture>
				pictureId);

		void onPictureChanged(EventedProject project, Identifier<? extends Picture>
				pictureId);
	}
}
