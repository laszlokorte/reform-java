package reform.stage.elements.factory;

import reform.core.forms.*;
import reform.core.procedure.Paper;
import reform.stage.elements.Entity;
import reform.stage.elements.entities.*;

public class EntityFactory
{

	public Entity createEntityFor(final Form form)
	{
		if (form.getClass() == Paper.class)
		{
			return new PaperEntity(((Paper) form).getId());
		}
		else if (form.getClass() == ArcForm.class)
		{
			return new ArcEntity(((ArcForm) form).getId());
		}
		else if (form.getClass() == PieForm.class)
		{
			return new PieEntity(((PieForm) form).getId());
		}
		else if (form.getClass() == LineForm.class)
		{
			return new LineEntity(((LineForm) form).getId());
		}
		else if (form.getClass() == CircleForm.class)
		{
			return new CircleEntity(((CircleForm) form).getId());
		}
		else if (form.getClass() == RectangleForm.class)
		{
			return new RectangleEntity(((RectangleForm) form).getId());
		}

		return new UnknownEntity(form.getId());
	}

}
