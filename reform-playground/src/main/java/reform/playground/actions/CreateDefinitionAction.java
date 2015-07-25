package reform.playground.actions;

import reform.components.expression.ExpressionEditor;
import reform.data.sheet.Definition;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.evented.core.EventedSheet;
import reform.identity.IdentifierEmitter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateDefinitionAction extends AbstractAction
{
	private final ExpressionEditor.Parser _parser;
	private final EventedSheet _eSheet;
	private final IdentifierEmitter _idEmitter;

	public CreateDefinitionAction(final ExpressionEditor.Parser parser, final
	EventedSheet eSheet, final IdentifierEmitter idEmitter)
	{
		_parser = parser;
		_eSheet = eSheet;
		_idEmitter = idEmitter;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eSheet.addDefinition(
				new Definition(_idEmitter.emit(), _parser.getUniqueNameFor("param",
				                                                           null),
				               new ConstantExpression(new Value(0))));
	}
}
