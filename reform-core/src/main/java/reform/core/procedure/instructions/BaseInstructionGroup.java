package reform.core.procedure.instructions;

import reform.core.analyzer.Analyzable;
import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.core.runtime.errors.UnexpectedInternalError;
import reform.identity.Identifier;

import java.util.ArrayList;
import java.util.Iterator;

abstract public class BaseInstructionGroup extends BaseInstruction implements
		InstructionGroup
{

	private final ArrayList<Instruction> _children = new ArrayList<>();

	protected BaseInstructionGroup()
	{
		_children.add(new NullInstruction(this));
	}

	@Override
	public Iterator<Instruction> iterator()
	{
		return _children.iterator();
	}

	@Override
	public int size()
	{
		return _children.size();
	}

	@Override
	public Instruction get(final int index)
	{
		return _children.get(index);
	}

	@Override
	public int indexOf(final Instruction instruction)
	{
		return _children.indexOf(instruction);
	}

	@Override
	public void insertInstruction(final Instruction instruction, final Position pos,
	                              final Instruction base)
	{
		final int index = _children.indexOf(base) + pos.offset;
		_children.add(index, instruction);
		instruction.setParent(this);
	}

	@Override
	public void removeInstruction(final Instruction instruction)
	{
		_children.remove(instruction);
		instruction.setParent(null);
	}

	@Override
	public void append(final Instruction instruction)
	{
		_children.add(instruction);
		instruction.setParent(this);
	}

	protected void _evaluateChildren(final Runtime runtime)
	{
		try
		{
			runtime.pushScope();
			for (int i = 0, j = _children.size(); i < j; i++)
			{
				if (runtime.shouldStop())
				{
					return;
				}
				final Instruction instruction = _children.get(i);
				runtime.beforeEval(instruction);
				try
				{
					instruction.evaluate(runtime);
				} catch (final RuntimeException e)
				{
					runtime.reportError(instruction, new UnexpectedInternalError(e));
				}
				runtime.afterEval(instruction);
			}
		} finally
		{
			runtime.popScope();
		}
	}

	protected void _analyzeChildren(final Analyzer analyzer)
	{
		analyzer.pushScope();
		for (int i = 0, j = _children.size(); i < j; i++)
		{
			final Analyzable instruction = _children.get(i);
			instruction.analyze(analyzer);
		}
		analyzer.popScope();
	}

	@Override
	public Identifier<? extends Form> getTarget()
	{
		return null;
	}
}
