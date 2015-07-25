package reform.stage.elements;

import reform.core.runtime.Runtime;

import java.util.List;

public interface InstructionControl
{
	List<ControlPoint> getControlPoints();

	RubberBand getRubberBand();

	void updateForRuntime(Runtime runtime);

	boolean canEdit();
}
