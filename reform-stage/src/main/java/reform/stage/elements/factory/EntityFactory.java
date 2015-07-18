package reform.stage.elements.factory;

import reform.core.forms.ArcForm;
import reform.core.forms.CircleForm;
import reform.core.forms.Form;
import reform.core.forms.LineForm;
import reform.core.forms.PieForm;
import reform.core.forms.RectangleForm;
import reform.core.procedure.Paper;
import reform.stage.elements.Entity;
import reform.stage.elements.entities.ArcEntity;
import reform.stage.elements.entities.CircleEntity;
import reform.stage.elements.entities.LineEntity;
import reform.stage.elements.entities.PaperEntity;
import reform.stage.elements.entities.PieEntity;
import reform.stage.elements.entities.RectangleEntity;
import reform.stage.elements.entities.UnknownEntity;

public class EntityFactory {

	public Entity createEntityFor(final Form form) {
		if (form.getClass() == Paper.class) {
			return new PaperEntity(((Paper) form).getId());
		} else if (form.getClass() == ArcForm.class) {
			return new ArcEntity(((ArcForm) form).getId());
		} else if (form.getClass() == PieForm.class) {
			return new PieEntity(((PieForm) form).getId());
		} else if (form.getClass() == LineForm.class) {
			return new LineEntity(((LineForm) form).getId());
		} else if (form.getClass() == CircleForm.class) {
			return new CircleEntity(((CircleForm) form).getId());
		} else if (form.getClass() == RectangleForm.class) {
			return new RectangleEntity(((RectangleForm) form).getId());
		}

		return new UnknownEntity(form.getId());
	}

}
