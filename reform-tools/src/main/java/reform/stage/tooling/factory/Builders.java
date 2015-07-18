package reform.stage.tooling.factory;

import reform.core.forms.ArcForm;
import reform.core.forms.CircleForm;
import reform.core.forms.LineForm;
import reform.core.forms.PieForm;
import reform.core.forms.RectangleForm;
import reform.identity.Identifier;
import reform.naming.Name;

public final class Builders {
	public static final FormFactory.Builder<LineForm> Line = (id, name) -> LineForm.construct(id, name);

	public static final FormFactory.Builder<RectangleForm> Rectangle = (id, name) -> RectangleForm.construct(id, name);

	public static final FormFactory.Builder<CircleForm> Circle = (id, name) -> CircleForm.construct(id, name);

	public static final FormFactory.Builder<PieForm> Pie = (id, name) -> PieForm.construct(id, name);

	public static final FormFactory.Builder<ArcForm> Arc = (id, name) -> ArcForm.construct(id, name);
}
