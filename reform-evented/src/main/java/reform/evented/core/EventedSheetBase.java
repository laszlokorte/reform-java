package reform.evented.core;

import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.expression.Expression;

import java.util.ArrayList;

public abstract class EventedSheetBase implements EventedSheet
{

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	final EventedPicture _evtPicture;

	abstract Sheet getSheet();

	public EventedSheetBase(final EventedPicture evtPicture)
	{
		_evtPicture = evtPicture;
	}

	public int size()
	{
		return getSheet().size();
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
		return getSheet().get(index).getName();
	}

	public String getUniqueNameFor(String wantedName, Definition def) {
		Sheet sheet = getSheet();

		String testName = wantedName;
		int postfix=0;
		Definition otherDef = sheet.findDefinitionWithName(testName);
		while(otherDef != null && otherDef != def) {
			testName = wantedName + ++postfix;
			otherDef = sheet.findDefinitionWithName(testName);
		}

		if(postfix > 0) {
			return testName;
		} else {
			return wantedName;
		}
	}

	public void setName(final int index, String name)
	{
		Sheet sheet = getSheet();
		Definition def = sheet.get(index);

		def.setName(name);

		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onNameChanged(this, def.getId(), index);
		}

		_evtPicture.propagateProcedureChange();
	}

	public Expression getExpression(final int index)
	{
		return getSheet().get(index).getExpression();
	}

	public Definition getDefinition(int index)
	{
		return getSheet().get(index);
	}


	public void setExpression(final int index, Expression expression)
	{
		Sheet sheet = getSheet();
		Definition def = getSheet().get(index);
		def.setExpression(expression);
		sheet.markDirty();
		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onDefinitionChanged(this, def.getId(), index);
		}
		_evtPicture.propagateProcedureChange();

	}


	public void addDefinition(final Definition definition)
	{
		Sheet sheet = getSheet();
		sheet.add(definition);
		sheet.markDirty();
		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onDefinitionAdded(this, definition, sheet.size() - 1);
		}
		_evtPicture.propagateProcedureChange();

	}


	public void removeDefinition(final int index)
	{
		Sheet sheet = getSheet();
		Definition def = sheet.get(index);
		sheet.remove(index);

		for(int i=0,j=_listeners.size();i<j;i++) {
			_listeners.get(i).onDefinitionRemoved(this, def, index);
		}
	}


	public Sheet getRaw()
	{
		return getSheet();
	}
}
