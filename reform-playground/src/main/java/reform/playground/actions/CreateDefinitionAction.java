package reform.playground.actions;

import reform.components.expression.ExpressionEditor;
import reform.data.sheet.Definition;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.evented.core.EventedDataSheet;
import reform.evented.core.EventedSheet;
import reform.identity.IdentifierEmitter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class CreateDefinitionAction extends AbstractAction
{
	private final ExpressionEditor.Parser _parser;
	private final EventedSheet _eSheet;
	private final IdentifierEmitter _idEmitter;

	public CreateDefinitionAction(ExpressionEditor.Parser parser, EventedSheet eSheet, IdentifierEmitter idEmitter) {
		_parser = parser;
		_eSheet = eSheet;
		_idEmitter = idEmitter;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eSheet.addDefinition(new Definition(_idEmitter.emit(), _parser.getUniqueNameFor("param", null), new ConstantExpression(new Value(0))));
	}
}
