package reform.core.forms;

import reform.core.forms.anchors.Anchor;
import reform.core.forms.relations.ExposedPoint;
import reform.core.graphics.DrawingType;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;
import reform.naming.Name;

public abstract class BaseForm<T extends Form> implements Form {
	private final transient IdentifiableList<ExposedPoint> _snapPoints = new IdentifiableList<>();
	private final transient IdentifiableList<Anchor> _anchors = new IdentifiableList<>();
	private final transient int _size;
	private final Identifier<T> _id;
	private final Name _name;
	private DrawingType _type = DrawingType.Draw;

	BaseForm(final Identifier<T> id, final int size,
             final Name name) {
		_id = id;
		_size = size;
		_name = name;
	}

	void addSnapPoint(final ExposedPoint point) {
		_snapPoints.add(point);
	}

	void addAnchor(final Anchor point) {
		_anchors.add(point);
	}

	@Override
	public Identifier<T> getId() {
		return _id;
	}

	@Override
	public int getSizeOnStack() {
		return _size;
	}

	@Override
	public ReferencePoint getPoint(
			final Identifier<? extends ExposedPoint> pointId) {
		return _snapPoints.getById(pointId);
	}

	@Override
	public Iterable<Identifier<? extends ExposedPoint>> getPoints() {
		return _snapPoints;
	}

	@Override
	public Name getName() {
		return _name;
	}

	@Override
	public DrawingType getType() {
		return _type;
	}

	@Override
	public Anchor getAnchor(final Identifier<? extends Anchor> anchorId) {
		return _anchors.getById(anchorId);
	}

	@Override
	public Iterable<Identifier<? extends Anchor>> getAnchors() {
		return _anchors;
	}

	@Override
	public void setType(final DrawingType type) {
		_type = type;
	}

}
