package reform.evented.core;

import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.expression.Expression;
import reform.identity.Identifier;

import java.util.ArrayList;

public class EventedSheet
{

	public interface Listener
	{
		void onNameChanged(EventedSheet picture, Identifier<? extends Definition> dataDefinition, int index);

		void onDefinitionChanged(EventedSheet picture, Identifier<? extends Definition> dataDefinition, int index);

		void onDefinitionAdded(EventedSheet eventedSheet, Definition definition, int index);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final EventedPicture _evtPicture;

	public EventedSheet(final EventedPicture evtPicture)
	{
		_evtPicture = evtPicture;
	}

	public int size()
	{
		return _evtPicture.getSheet().size();
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}


	public String getName(final int index)
	{
		return _evtPicture.getSheet().get(index).getName();
	}

	public void setUniqueName(final int index, String name)
	{
		Sheet sheet = _evtPicture.getSheet();
		Definition def = sheet.get(index);

		String wantedName = name;
		String testName = name;
		int postfix=0;
		Definition otherDef = sheet.findDefinitionWithName(testName);
		while(otherDef != null && otherDef != def) {
			otherDef = sheet.findDefinitionWithName(testName);
			testName = wantedName + ++postfix;
		}

		if(postfix > 0) {
			def.setName(testName);
		} else {
			def.setName(wantedName);
		}

		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onNameChanged(this, def.getId(), index);
		}
	}

	public Expression getExpression(final int index)
	{
		return _evtPicture.getSheet().get(index).getExpression();
	}

	public Definition getDefinition(int index)
	{
		return _evtPicture.getSheet().get(index);
	}


	public void setExpression(final int index, Expression expression)
	{
		Sheet sheet = _evtPicture.getSheet();
		Definition def = _evtPicture.getSheet().get(index);
		def.setExpression(expression);
		sheet.markDirty();
		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onDefinitionChanged(this, def.getId(), index);
		}
	}


	public void addDefinition(final Definition definition)
	{
		Sheet sheet = _evtPicture.getSheet();
		sheet.add(definition);
		sheet.markDirty();
		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onDefinitionAdded(this, definition, sheet.size() - 1);
		}
	}

	public Sheet getRaw()
	{
		return _evtPicture.getSheet();
	}
}
