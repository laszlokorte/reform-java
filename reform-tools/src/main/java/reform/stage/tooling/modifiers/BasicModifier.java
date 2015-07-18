package reform.stage.tooling.modifiers;


public class BasicModifier implements Modifier {
	private boolean _state = false;

	public BasicModifier() {
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
