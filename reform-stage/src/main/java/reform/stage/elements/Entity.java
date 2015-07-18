package reform.stage.elements;

import java.awt.Shape;
import java.util.List;

import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.identity.Identifier;
import reform.math.Vec2;
import reform.stage.elements.outline.EntityOutline;

public interface Entity {

	void updateForRuntime(Runtime runtime);

	public List<EntityPoint> getSnapPoints();

	public List<Handle> getHandles();

	boolean contains(Vec2 position);

	Identifier<? extends Form> getId();

	EntityOutline getOutline();

	Shape getShape();

	boolean isGuide();

}
