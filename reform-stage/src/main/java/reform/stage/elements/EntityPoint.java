package reform.stage.elements;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.ForeignFormsPoint;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.math.Vector;

public class EntityPoint implements SnapPoint
{
	private static final double SNAP_RADIUS2 = SnapPoint.SNAP_RADIUS * SnapPoint
			.SNAP_RADIUS;
	private static final double GRAB_RADIUS = 10;
	private static final double GRAB_RADIUS2 = GRAB_RADIUS * GRAB_RADIUS;

	private final Identifier<? extends Form> _formId;
	private final Identifier<? extends ExposedPoint> _pointId;
	private final Vec2 _value = new Vec2();
	private final StringBuilder _label = new StringBuilder(50);

	public <T extends Form> EntityPoint(final Identifier<T> formId, final
	ExposedPointToken<? super T> token)
	{
		_formId = formId;
		_pointId = new Identifier<>(token);
	}

	@Override
	public double getX()
	{
		return _value.x;
	}

	@Override
	public double getY()
	{
		return _value.y;
	}

	@Override
	public ReferencePoint createReference()
	{
		return new ForeignFormsPoint(getFormId(), getPointId());
	}

	public String getLabel()
	{
		return _label.toString();
	}

	public boolean isInSnapRadius(final double x, final double y)
	{
		return Vector.distance2(x, y, _value.x, _value.y) < SNAP_RADIUS2;
	}

	public Identifier<? extends Form> getFormId()
	{
		return _formId;
	}

	public Identifier<? extends ExposedPoint> getPointId()
	{
		return _pointId;
	}

	public void updateForRuntime(final Runtime runtime, final Analyzer analyzer)
	{
		final Form form = runtime.get(_formId);
		final ReferencePoint p = form.getPoint(_pointId);
		_value.set(p.getXValueForRuntime(runtime), p.getYValueForRuntime(runtime));

		_label.setLength(0);
		_label.append(analyzer.getForm(_formId).getName().getValue());
		_label.append("'s ");
		_label.append(p.getDescription(analyzer));
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final EntityPoint other = (EntityPoint) obj;

		return _formId.equals(other._formId) && _pointId.equals(other._pointId);
	}

	public boolean isInGrabRadius(final double x, final double y)
	{
		return Vector.distance2(x, y, _value.x, _value.y) < GRAB_RADIUS2;
	}
}
