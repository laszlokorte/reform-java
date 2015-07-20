package reform.core.project;

import reform.identity.FastIterable;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;

public final class Project
{
	private final IdentifiableList<Picture> _pictures = new IdentifiableList<>();

	public void addPicture(final Picture picture)
	{
		_pictures.add(picture);
	}

	public void removePicture(final Identifier<? extends Picture> pictureId)
	{
		_pictures.removeById(pictureId);
	}

	public Picture getPicture(final Identifier<? extends Picture> pictureId)
	{
		return _pictures.getById(pictureId);
	}

	public FastIterable<Identifier<? extends Picture>> getPictures()
	{
		return _pictures;
	}

	public Identifier<? extends Picture> getPictureAtIndex(final int index)
	{
		return _pictures.getIdAtIndex(index);
	}

	public boolean containsPicture(final Identifier<? extends Picture> pictureId)
	{
		return _pictures.contains(pictureId);
	}

	public int getPictureCount()
	{
		return _pictures.size();
	}
}
