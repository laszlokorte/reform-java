package reform.stage.tooling.modifiers;


public class BasicModifier implements Modifier {
	private final String _name;
	private boolean _state = false;

	public BasicModifier(final String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	@Override
	public void setState(final boolean newState) {
		if (_state != newState) {
			_state = newState;
		}
	}

	@Override
	public boolean isActive() {
		return _state;
	}

}
