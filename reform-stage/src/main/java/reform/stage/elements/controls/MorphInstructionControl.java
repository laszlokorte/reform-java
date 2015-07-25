package reform.stage.elements.controls;

import reform.core.forms.Form;
import reform.core.forms.anchors.Anchor;
import reform.core.procedure.instructions.single.MorphInstruction;
import reform.core.runtime.relations.TranslationDistance;
import reform.identity.Identifier;
import reform.stage.elements.ControlPoint;
import reform.stage.elements.InstructionControl;
import reform.stage.elements.RubberBand;

import java.util.ArrayList;
import java.util.List;

public class MorphInstructionControl implements InstructionControl
{
	private final MorphInstruction _instruction;

	private final ArrayList<ControlPoint> _controlPoints = new ArrayList<>();

	private final ControlPoint _sourcePoint = new ControlPoint();
	private final ControlPoint _targetPoint = new ControlPoint();
	private final RubberBand _rubberBand = new RubberBand(_sourcePoint, _targetPoint);

	private boolean _canEdit = false;

	public MorphInstructionControl(final MorphInstruction instruction)
	{
		_instruction = instruction;
		_controlPoints.add(_sourcePoint);
		_controlPoints.add(_targetPoint);
	}

	public MorphInstruction getInstruction()
	{
		return _instruction;
	}

	@Override
	public List<ControlPoint> getControlPoints()
	{
		return _controlPoints;
	}

	@Override
	public RubberBand getRubberBand()
	{
		return _rubberBand;
	}

	@Override
	public void updateForRuntime(final reform.core.runtime.Runtime runtime)
	{
		final TranslationDistance distance = _instruction.getDistance();

		final Identifier<? extends Form> formId = _instruction.getFormId();
		final Form form = runtime.get(formId);
		if (form != null)
		{
			final Anchor anchor = form.getAnchor(_instruction.getAnchorId());
			if (anchor != null)
			{
				final double baseX = anchor.getXValueForRuntime(runtime);
				final double baseY = anchor.getYValueForRuntime(runtime);

				_sourcePoint.setError(false);
				_sourcePoint.updatePosition(baseX, baseY);

				if (distance.isValidFor(runtime))
				{
					_targetPoint.setError(false);
					_targetPoint.updatePosition(baseX + distance.getXValueForRuntime(runtime),
					                            baseY + distance.getYValueForRuntime(runtime));
				}
				else
				{
					_targetPoint.setError(true);
					_targetPoint.updatePosition(100, 100);
				}

				_canEdit = true;
			}
			else
			{
				_sourcePoint.setError(true);
				_sourcePoint.updatePosition(50, 50);
				_canEdit = false;
			}
		}
		else
		{
			_canEdit = false;
		}
	}


	public boolean canEdit()
	{
		return _canEdit;
	}

}
