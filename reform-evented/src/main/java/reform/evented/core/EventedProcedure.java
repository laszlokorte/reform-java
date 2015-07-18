package reform.evented.core;

import java.util.ArrayList;

import reform.core.analyzer.ProjectAnalyzer;
import reform.core.forms.Form;
import reform.core.procedure.Procedure;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.InstructionGroup.Position;
import reform.core.runtime.Runtime;

public class EventedProcedure {

	public interface Listener {
		void onInstructionAdded(EventedProcedure procedure,
                                Instruction instruction, InstructionGroup parent);

		void onInstructionRemoved(EventedProcedure procedure,
                                  Instruction instruction, InstructionGroup parent);

        void onInstructionWillBeRemoved(EventedProcedure procedure,
                                        Instruction instruction, InstructionGroup parent);

		void onInstructionChanged(EventedProcedure procedure,
                                  Instruction instruction, InstructionGroup parent);

		void onFormChanged(EventedProcedure procedure, Form form);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final EventedPicture _evtPicture;

	public EventedProcedure(final EventedPicture evtPicture) {
		_evtPicture = evtPicture;
	}

	public void addInstruction(final Instruction instruction,
			final InstructionGroup.Position pos, final Instruction base) {
		final InstructionGroup parent = base.getParent();

		_evtPicture.getProcedure().addInstruction(instruction, pos, base);
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onInstructionAdded(this, instruction, parent);
		}
		_evtPicture.propagateProcedureChange();
	}

	public void removeInstruction(final Instruction instruction) {
		final Procedure proc = _evtPicture.getProcedure();
		if (proc.canRemoveInstruction(instruction)) {
			final InstructionGroup parent = instruction.getParent();
			for (int i = 0, j = _listeners.size(); i < j; i++) {
				_listeners.get(i).onInstructionWillBeRemoved(this, instruction,
						parent);
			}
			proc.removeInstruction(instruction);
			for (int i = 0, j = _listeners.size(); i < j; i++) {
				_listeners.get(i).onInstructionRemoved(this, instruction,
						parent);
			}
			_evtPicture.propagateProcedureChange();
		}
	}

	public boolean canRemoveInstruction(final Instruction instruction) {
		return _evtPicture.getProcedure().canRemoveInstruction(instruction);
	}

	public void publishInstructionChange(final Instruction instruction) {
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onInstructionChanged(this, instruction,
					instruction.getParent());
		}
		_evtPicture.propagateProcedureChange();
	}

	public void addListener(final Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener) {
		_listeners.remove(listener);
	}

	public void evaluate(final Runtime runtime) throws InterruptedException {
		_evtPicture.getProcedure().evaluate(runtime);
	}

	public InstructionGroup getRoot() {
		return _evtPicture.getProcedure().getRoot();
	}

	public Procedure getRaw() {
		return _evtPicture.getProcedure();
	}

	public boolean exists() {
		return _evtPicture.exists();
	}

	public void analyze(final ProjectAnalyzer analyzer) {
		_evtPicture.getProcedure().analyze(analyzer);
	}

	public void wrapInstruction(final Instruction instruction,
			final InstructionGroup group) {
		final InstructionGroup parent = instruction.getParent();

		final Instruction base = parent.get(parent.indexOf(instruction) - 1);
		_evtPicture.getProcedure().removeInstruction(instruction);
		group.append(instruction);
		_evtPicture.getProcedure().addInstruction(group, Position.After, base);
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onInstructionAdded(this, instruction, parent);
		}
		_evtPicture.propagateProcedureChange();
	}

	public void publishFormChange(final Form form) {
		for (int i = 0, j = _listeners.size(); i < j; i++) {
			_listeners.get(i).onFormChanged(this, form);
		}
		_evtPicture.propagateProcedureChange();
	}

}
