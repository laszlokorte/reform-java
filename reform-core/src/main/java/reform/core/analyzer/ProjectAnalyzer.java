package reform.core.analyzer;

import reform.core.forms.Form;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;

import java.util.ArrayList;

public class ProjectAnalyzer implements Analyzer
{

	public interface Listener
	{
		void onFinishAnalysis(ProjectAnalyzer analyzer);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private int _depth = -1;
	private final ArrayList<FlatNode> _nodes = new ArrayList<>();
	private final IdentifiableList<Form> _forms = new IdentifiableList<>();

	@Override
	public void begin()
	{
		_depth = -1;
		_nodes.clear();
		_forms.clear();

	}

	@Override
	public void finish()
	{
		if (_depth != -1)
		{
			throw new RuntimeException("Depth must be 0");
		}

		synchronized (_listeners)
		{
			for (int i = 0; i < _listeners.size(); i++)
			{
				_listeners.get(i).onFinishAnalysis(this);
			}
		}
	}

	@Override
	public void publish(final Analyzable source, final String label)
	{
		_nodes.add(new FlatNode(_depth, label, source, false));

	}

	@Override
	public void publishGroup(final Analyzable source, final String label)
	{
		_nodes.add(new FlatNode(_depth, label, source, true));

	}

	@Override
	public void pushScope()
	{
		_depth += 1;
	}

	@Override
	public void popScope()
	{
		_depth -= 1;
	}

	@Override
	public void announceForm(final Form form)
	{
		_forms.add(form);
	}

	@Override
	public Form getForm(final Identifier<? extends Form> formId)
	{
		return _forms.getById(formId);
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public FlatNode getNode(final int index)
	{
		return _nodes.get(index);
	}

	public int getNodeCount()
	{
		return _nodes.size();
	}

	public int indexOf(final Analyzable source)
	{
		for (int i = 0; i < _nodes.size(); i++)
		{
			if (_nodes.get(i).getSource() == source)
			{
				return i;
			}
		}
		return -1;
	}

}
