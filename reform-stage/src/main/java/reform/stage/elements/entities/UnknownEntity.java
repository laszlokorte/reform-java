package reform.stage.elements.entities;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.Handle;
import reform.stage.elements.outline.EntityOutline;

public class UnknownEntity implements Entity {
	private final Identifier<? extends Form> _formId;

	private final EntityOutline _outline;

	private final GeneralPath.Double _shape = new GeneralPath.Double();

	private final ArrayList<EntityPoint> _points = new ArrayList<>();
	private final ArrayList<Handle> _handles = new ArrayList<>();

    private String _label = "Unknown";

    private boolean _isGuide = false;

	public UnknownEntity(final Identifier<? extends Form> formId) {
		_formId = formId;
		_outline = new EntityOutline.Null(formId);
	}

	@Override
	public void updateForRuntime(final Runtime runtime, Analyzer analyzer) {
		_isGuide = runtime.get(_formId).getType() == DrawingType.Guide;
	}

	@Override
	public List<EntityPoint> getSnapPoints() {
		return _points;
	}

	@Override
	public List<Handle> getHandles() {
		return _handles;
	}

	@Override
	public boolean contains(final Vec2 position) {
		return false;
	}

	@Override
	public Identifier<? extends Form> getId() {
		return _formId;
	}

	@Override
	public EntityOutline getOutline() {
		return _outline;
	}

	@Override
	public Shape getShape() {
		return _shape;
	}

	@Override
	public boolean isGuide() {
		return _isGuide;
	}


    @Override
    public String getLabel() {
        return _label;
    }

}
