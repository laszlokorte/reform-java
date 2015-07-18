package reform.core.runtime.stack;

import reform.core.forms.Form;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;

class Frame {
	private final Stack _stack;
	final IdentifiableList<Form> _content = new IdentifiableList<>();

	public Frame(final Stack stack) {
		_stack = stack;
	}

	void declare(final Form element) {
		_content.add(element);
	}

	void popped() {
		for (int i = _content.size() - 1; i >= 0; i--) {
			final Identifier<? extends Form> id = _content.getIdAtIndex(i);
			_stack.remove(id);
		}
	}
}
