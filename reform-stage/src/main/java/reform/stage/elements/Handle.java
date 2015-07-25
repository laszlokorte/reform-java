package reform.stage.elements;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.relations.ExposedPoint;
import reform.core.forms.relations.ExposedPoint.ExposedPointToken;
import reform.core.forms.relations.ForeignFormsPoint;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.math.Vec2;
import reform.math.Vector;

public class Handle
{
	public static final double GRAB_RADIUS = 10;
	private static final double GRAB_RADIUS2 = GRAB_RADIUS * GRAB_RADIUS;

	private final Identifier<? extends Form> _formId;
	private final Identifier<? extends ExposedPoint> _pointId;
	private final Identifier<? extends Anchor> _anchorId;
	private final Vec2 _value = new Vec2();

	private final PivotPair _pivot;
	private final StringBuilder _label = new StringBuilder();

	public <T extends Form> Handle(final Identifier<T> formId, final ExposedPointToken<?
			super T> pointToken, final IdentityToken anchorToken, final PivotPair pivot)
	{
		_formId = formId;
		_pointId = new Identifier<>(pointToken);
		_anchorId = new Identifier<>(anchorToken);
		_pivot = pivot;
	}

	public <T extends Form> Handle(final Identifier<T> formId, final ExposedPointToken<?
			super T> pointToken, final IdentityToken anchorToken)
	{
		this(formId, pointToken, anchorToken, null);
	}

	public double getX()
	{
		return _value.x;
	}

	public double getY()
	{
		return _value.y;
	}

	public Identifier<? extends Anchor> getAnchorId()
	{
		return _anchorId;
	}

	public Identifier<? extends Form> getFormId()
	{
		return _formId;
	}

	public void updateForRuntime(final Runtime runtime, final Analyzer analyzer)
	{
		final ReferencePoint p = runtime.get(_formId).getPoint(_pointId);
		_value.set(p.getXValueForRuntime(runtime), p.getYValueForRuntime(runtime));

		_label.setLength(0);
		_label.append(analyzer.getForm(_formId).getName().getValue());
		_label.append("'s ");
		_label.append(p.getDescription(analyzer));
	}

	public PivotPair getPivot()
	{
		return _pivot;
	}

	public boolean hasPivot()
	{
		return _pivot != null;
	}

	public ReferencePoint createReference()
	{
		return new ForeignFormsPoint(_formId, _pointId);
	}

	public String getLabel()
	{
		return _label.toString();
	}

	public boolean isInGrabRadius(final double x, final double y)
	{
		return Vector.distance2(x, y, _value.x, _value.y) < GRAB_RADIUS2;
	}
}
