package reform.core.analyzer;

public class FlatNode
{
	private final int _indentation;
	private final boolean _isGroup;
	private final String _label;
	private final Analyzable _source;

	public FlatNode(final int indentation, final String label, final Analyzable source,
	                final boolean isGroup)
	{
		_indentation = indentation;
		_label = label;
		_source = source;
		_isGroup = isGroup;
	}

	public int getIndentation()
	{
		return _indentation;
	}

	public String getLabel()
	{
		return _label;
	}

	public Analyzable getSource()
	{
		return _source;
	}

	public boolean isGroup()
	{
		return _isGroup;
	}
}
