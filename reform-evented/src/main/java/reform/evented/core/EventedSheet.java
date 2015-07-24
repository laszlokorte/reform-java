package reform.evented.core;

import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.expression.Expression;
import reform.identity.Identifier;

public interface EventedSheet
{
	public interface Listener
	{
		void onNameChanged(EventedSheet eventedSheet, Identifier<? extends Definition> dataDefinition, int index);

		void onDefinitionChanged(EventedSheet eventedSheet, Identifier<? extends Definition> dataDefinition, int
				index);

		void onDefinitionAdded(EventedSheet eventedSheet, Definition definition, int index);

		void onDefinitionRemoved(EventedSheet eventedSheet, Definition definition, int index);
	}

	public int size();


	public void removeListener(final Listener listener);

	public void addListener(final Listener listener);


	public String getName(final int index);

	public void setName(final int index, String name);

	public Expression getExpression(final int index);

	public Definition getDefinition(int index);


	public void setExpression(final int index, Expression expression);


	public void addDefinition(final Definition definition);


	public void removeDefinition(final int index);


	public Sheet getRaw();
}
