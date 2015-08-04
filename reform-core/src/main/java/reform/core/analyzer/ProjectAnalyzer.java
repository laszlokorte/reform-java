package reform.core.analyzer;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProjectAnalyzer implements Analyzer
{
	private final Set<Identifier<?extends Picture>> _dependencies = new HashSet<>();

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private final ArrayList<FlatNode> _nodes = new ArrayList<>();
	private final IdentifiableList<Form> _forms = new IdentifiableList<>();
	private int _depth = -1;

	@Override
	public void begin()
	{
		_depth = -1;
		_nodes.clear();
		_forms.clear();
		_dependencies.clear();

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
			for (int i = 0, j = _listeners.size(); i < j; i++)
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
	public void announceDepencency(final Identifier<? extends Picture> pictureId)
	{
		_dependencies.add(pictureId);
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
		for (int i = 0, j = _nodes.size(); i < j; i++)
		{
			if (_nodes.get(i).getSource() == source)
			{
				return i;
			}
		}
		return -1;
	}

	public boolean dependsOn(Identifier<?extends Picture> pictureId) {
		return _dependencies.contains(pictureId);
	}

	public interface Listener
	{
		void onFinishAnalysis(ProjectAnalyzer analyzer);
	}

}
