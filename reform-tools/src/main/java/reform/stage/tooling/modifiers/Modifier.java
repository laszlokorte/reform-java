package reform.stage.tooling.modifiers;

public interface Modifier
{
	void setState(final boolean newState);

	boolean isActive();
}
