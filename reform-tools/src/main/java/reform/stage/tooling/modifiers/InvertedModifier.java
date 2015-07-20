package reform.stage.tooling.modifiers;

public class InvertedModifier implements Modifier
{

	private final Modifier _originalModifier;

	public InvertedModifier(final Modifier originalModifier)
	{
		_originalModifier = originalModifier;
	}

	@Override
	public void setState(final boolean newState)
	{
		_originalModifier.setState(!newState);
	}

	@Override
	public boolean isActive()
	{
		return !_originalModifier.isActive();
	}

}
