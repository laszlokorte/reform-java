package reform.data.sheet;

import reform.data.sheet.expression.ReferenceExpression;
import reform.identity.Identifier;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Sheet
{
	private final ArrayList<Definition> _definitions = new ArrayList<>();
	transient private final ArrayList<Definition> _sortedDefinitions = new ArrayList<>();
	transient private final Set<Definition> _unresolved = new HashSet<>();
	transient private boolean _fresh = false;
	final transient private Set<Definition> _duplicates = new HashSet<>();

	public int size()
	{
		return _definitions.size();
	}

	public Definition get(final int index)
	{
		return _definitions.get(index);
	}

	public int sortedSize()
	{
		return _sortedDefinitions.size();
	}

	public Definition sortedGet(final int index)
	{
		return _sortedDefinitions.get(index);
	}

	public void add(final Definition def)
	{
		_definitions.add(def);
		_fresh = false;
	}


	public void set(final int index, final Definition definition)
	{
		_definitions.set(index, definition);
		_fresh = false;
	}

	public boolean hasError(final Definition d)
	{
		return _unresolved.contains(d);
	}

	public boolean isDuplicate(final Definition d)
	{
		return _duplicates.contains(d);
	}

	public void markDirty()
	{
		_fresh = false;
	}

	void prepareForSolving()
	{
		final Set<Identifier<? extends Definition>> referencedIds = new HashSet<>();
		final Map<Identifier<? extends Definition>, Definition> definitionMap = new HashMap<>();
		_duplicates.clear();

		boolean fresh = _fresh;
		for (int i = 0, j = _definitions.size(); i < j; i++)
		{
			final Definition d = _definitions.get(i);
			fresh &= d.refreshDependencies();

			d._incoming = 0;
			if (definitionMap.containsKey(d.getId()))
			{
				_duplicates.add(d);
			}
			else
			{
				definitionMap.put(d.getId(), d);
			}
		}

		if (fresh)
		{
			return;
		}

		_unresolved.clear();
		for (int i = 0, j = _definitions.size(); i < j; i++)
		{
			final Definition d = _definitions.get(i);
			final Set<ReferenceExpression> currentReferences = d.getDependencies();
			for (final ReferenceExpression ref : currentReferences)
			{
				if (definitionMap.containsKey(ref.getId()))
				{
					ref.setLabel(definitionMap.get(ref.getId()).getName());
				}
				final Identifier<? extends Definition> id = ref.getId();
				final Definition definition = definitionMap.get(id);
				if (definition != null)
				{
					definition._incoming++;
				}
				else
				{
					referencedIds.add(d.getId());
					_unresolved.add(d);
				}
				referencedIds.add(id);
			}
		}

		final Queue<Definition> rootDefinitions = new LinkedBlockingQueue<>();

		for (int i = 0, j = _definitions.size(); i < j; i++)
		{
			final Definition d = _definitions.get(i);
			if (!referencedIds.contains(d.getId()))
			{
				rootDefinitions.add(d);
			}
		}

		_sortedDefinitions.clear();

		while (!rootDefinitions.isEmpty())
		{
			final Definition d = rootDefinitions.poll();
			final Set<ReferenceExpression> dependencies = d.getDependencies();
			final Iterator<ReferenceExpression> it = dependencies.iterator();
			_sortedDefinitions.add(d);
			while (it.hasNext())
			{
				final ReferenceExpression ref = it.next();
				final Definition dependency = definitionMap.get(ref.getId());
				if (dependency != null)
				{
					dependency._incoming--;
					if (dependency._incoming == 0)
					{
						rootDefinitions.add(dependency);
					}
				}
			}
		}

	}

	public void remove(final int index)
	{
		final Definition def = _definitions.remove(index);
		_unresolved.remove(def);
	}

	public Definition findDefinitionWithName(final String s)
	{
		for (int i = 0, j = _definitions.size(); i < j; i++)
		{
			final Definition d = _definitions.get(i);
			if (d.getName().equals(s))
			{
				return d;
			}
		}

		return null;
	}
}
