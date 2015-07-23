package reform.playground.presenter;

import reform.components.expression.ExpressionEditor;
import reform.core.forms.Form;
import reform.core.forms.relations.ConstantRotationAngle;
import reform.core.forms.relations.ConstantScaleFactor;
import reform.core.forms.relations.ExpressionRotationAngle;
import reform.core.forms.relations.ExpressionScaleFactor;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.blocks.ForLoopInstruction;
import reform.core.procedure.instructions.blocks.IfConditionInstruction;
import reform.core.procedure.instructions.single.RotateInstruction;
import reform.core.procedure.instructions.single.ScaleInstruction;
import reform.core.runtime.relations.RotationAngle;
import reform.core.runtime.relations.ScaleFactor;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.data.sheet.expression.Expression;
import reform.evented.core.EventedProcedure;
import reform.playground.views.sheet.ExpressionParser;
import reform.stage.tooling.InstructionFocus;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public final class InstructionsOptionPanel implements InstructionFocus.Listener, ChangeListener, EventedProcedure
		.Listener
{
	private final JPanel _panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private final JLabel _label = new JLabel("T:");

	private final ExpressionEditor _expressionEditor;
	private final JCheckBox _checkbox = new JCheckBox();

	private final InstructionFocus _focus;
	private final EventedProcedure _eProcedure;

	public InstructionsOptionPanel(final EventedProcedure eProcedure, final InstructionFocus focus, ExpressionParser expressionParser)
	{
		_expressionEditor = new ExpressionEditor(expressionParser);
		_eProcedure = eProcedure;
		_focus = focus;
		_panel.add(_label);
		_panel.add(_checkbox);
		_panel.add(_expressionEditor);

		_expressionEditor.setColumns(5);

		_checkbox.setFocusable(false);

		_expressionEditor.addChangeListener(this);
		_checkbox.addChangeListener(this);

		focus.addListener(this);
		onFocusChanged(focus);

		eProcedure.addListener(this);
	}

	public Component getComponent()
	{
		return _panel;
	}

	@Override
	public void onFocusChanged(final InstructionFocus focus)
	{
		boolean showCheckbox = false;
		boolean showExpression = false;

		if (focus.isSet())
		{
			if (focus.getFocused() instanceof ForLoopInstruction)
			{
				final ForLoopInstruction loop = (ForLoopInstruction) focus.getFocused();
				_label.setText("T:");
				_expressionEditor.setExpression(loop.getExpression());
				showExpression = true;
			}
			else if (focus.getFocused() instanceof ScaleInstruction)
			{
				final ScaleInstruction instruction = (ScaleInstruction) focus.getFocused();
				final ScaleFactor factor = instruction.getFactor();
				if (factor instanceof ConstantScaleFactor)
				{
					final ConstantScaleFactor f = (ConstantScaleFactor) factor;
					_label.setText("S:");
					_expressionEditor.setValue(f.getValue());
					showExpression = true;
				}else if(factor instanceof ExpressionScaleFactor) {
					ExpressionScaleFactor f = (ExpressionScaleFactor) factor;
					_label.setText("S:");
					_expressionEditor.setExpression(f.getExpression());
					showExpression = true;
				}
			}
			else if (focus.getFocused() instanceof RotateInstruction)
			{
				final RotateInstruction instruction = (RotateInstruction) focus.getFocused();
				final RotationAngle angle = instruction.getAngle();
				if (angle instanceof ConstantRotationAngle)
				{
					final ConstantRotationAngle a = (ConstantRotationAngle) angle;
					_label.setText("R:");
					_expressionEditor.setValue(a.getValue());
					showExpression = true;
				} else if(angle instanceof ExpressionRotationAngle) {
					ExpressionRotationAngle a = (ExpressionRotationAngle) angle;
					_label.setText("R:");
					_expressionEditor.setExpression(a.getExpression());
					showExpression = true;
				}
			}
			else if (focus.getFocused() instanceof IfConditionInstruction)
			{
				final IfConditionInstruction instruction = (IfConditionInstruction) focus.getFocused();
				final boolean condition = instruction.getCondition();
				_label.setText("C:");
				_checkbox.setSelected(condition);
				showCheckbox = true;
			}
		}

		_checkbox.setVisible(showCheckbox);
		_expressionEditor.setVisible(showExpression);
		_label.setVisible(showCheckbox || showExpression);
	}

	@Override
	public void stateChanged(final ChangeEvent e)
	{

		if (_focus.isSet())
		{
			if (_focus.getFocused() instanceof ForLoopInstruction)
			{
				final ForLoopInstruction loop = (ForLoopInstruction) _focus.getFocused();
				loop.setExpression(_expressionEditor.getExpression());
				_eProcedure.publishInstructionChange(loop);
			}
			else if (_focus.getFocused() instanceof ScaleInstruction)
			{
				final ScaleInstruction instruction = (ScaleInstruction) _focus.getFocused();
				final ScaleFactor factor = instruction.getFactor();

				if (factor instanceof ConstantScaleFactor)
				{

					SetFactor:
					{
						Expression expression = _expressionEditor.getExpression();

						if (expression instanceof ConstantExpression)
						{
							Value v = ((ConstantExpression) expression).getValue();
							if (v.type == Value.Type.Integer || v.type == Value.Type.Double)
							{
								((ConstantScaleFactor) factor).setFactor(v.getDouble());
								break SetFactor;
							}
						}

						instruction.setFactor(new ExpressionScaleFactor(_expressionEditor.getExpression()));
					}

					_eProcedure.publishInstructionChange(instruction);
				} else if(factor instanceof ExpressionScaleFactor) {
					final ExpressionScaleFactor f = (ExpressionScaleFactor) factor;
					f.setFactorExpression(_expressionEditor.getExpression());
					_eProcedure.publishInstructionChange(instruction);
				}

			}
			else if (_focus.getFocused() instanceof RotateInstruction)
			{
				final RotateInstruction instruction = (RotateInstruction) _focus.getFocused();
				final RotationAngle angle = instruction.getAngle();

				if (angle instanceof ConstantRotationAngle)
				{

					SetAngle:
					{
						Expression expression = _expressionEditor.getExpression();

						if (expression instanceof ConstantExpression)
						{
							Value v = ((ConstantExpression) expression).getValue();
							if (v.type == Value.Type.Integer || v.type == Value.Type.Double)
							{
								((ConstantRotationAngle) angle).setAngle(v.getDouble());
								break SetAngle;
							}
						}

						instruction.setAngle(new ExpressionRotationAngle(_expressionEditor.getExpression()));
					}

					_eProcedure.publishInstructionChange(instruction);
				} else if(angle instanceof ExpressionRotationAngle) {
					final ExpressionRotationAngle f = (ExpressionRotationAngle) angle;
					f.setAngleExpression(_expressionEditor.getExpression());
					_eProcedure.publishInstructionChange(instruction);
				}
			}
			else if (_focus.getFocused() instanceof IfConditionInstruction)
			{
				final IfConditionInstruction instruction = (IfConditionInstruction) _focus.getFocused();
				instruction.setCondition(_checkbox.isSelected());
				_eProcedure.publishInstructionChange(instruction);
			}
		}
	}

	@Override
	public void onInstructionAdded(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionWillBeRemoved(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onInstructionChanged(final EventedProcedure procedure, final Instruction instruction, final
	InstructionGroup parent)
	{
		if (_focus.isSet() && _focus.getFocused() == instruction)
		{
			onFocusChanged(_focus);
		}
	}

	@Override
	public void onFormChanged(final EventedProcedure procedure, final Form form)
	{
		// TODO Auto-generated method stub

	}
}
