package reform.evented.core;

import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.expression.Expression;
import reform.identity.Identifier;

public interface EventedSheet
{
	interface Listener
	{
		void onNameChanged(EventedSheet eventedSheet, Identifier<? extends Definition> dataDefinition, int index);

		void onDefinitionChanged(EventedSheet eventedSheet, Identifier<? extends Definition> dataDefinition, int
				index);

		void onDefinitionAdded(EventedSheet eventedSheet, Definition definition, int index);

		void onDefinitionRemoved(EventedSheet eventedSheet, Definition definition, int index);
	}

	int size();


	void removeListener(final Listener listener);

	void addListener(final Listener listener);


	String getName(final int index);

	void setName(final int index, String name);

	Expression getExpression(final int index);

	Definition getDefinition(int index);


	void setExpression(final int index, Expression expression);


	void addDefinition(final Definition definition);


	void removeDefinition(final int index);


	Sheet getRaw();
}
