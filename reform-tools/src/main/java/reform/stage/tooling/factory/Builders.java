package reform.stage.tooling.factory;

import reform.core.forms.ArcForm;
import reform.core.forms.CircleForm;
import reform.core.forms.LineForm;
import reform.core.forms.PieForm;
import reform.core.forms.RectangleForm;
import reform.identity.Identifier;
import reform.naming.Name;

public final class Builders {
	public static final FormFactory.Builder<LineForm> Line = new FormFactory.Builder<LineForm>() {
		@Override
		public LineForm build(final Identifier<LineForm> id, final Name name) {
			return LineForm.construct(id, name);
		}
	};

	public static final FormFactory.Builder<RectangleForm> Rectangle = new FormFactory.Builder<RectangleForm>() {
		@Override
		public RectangleForm build(final Identifier<RectangleForm> id,
				final Name name) {
			return RectangleForm.construct(id, name);
		}
	};

	public static final FormFactory.Builder<CircleForm> Circle = new FormFactory.Builder<CircleForm>() {
		@Override
		public CircleForm build(final Identifier<CircleForm> id, final Name name) {
			return CircleForm.construct(id, name);
		}
	};

	public static final FormFactory.Builder<PieForm> Pie = new FormFactory.Builder<PieForm>() {
		@Override
		public PieForm build(final Identifier<PieForm> id, final Name name) {
			return PieForm.construct(id, name);
		}
	};

	public static final FormFactory.Builder<ArcForm> Arc = new FormFactory.Builder<ArcForm>() {
		@Override
		public ArcForm build(final Identifier<ArcForm> id, final Name name) {
			return ArcForm.construct(id, name);
		}
	};
}
