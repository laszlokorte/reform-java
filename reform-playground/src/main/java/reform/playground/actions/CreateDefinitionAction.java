package reform.playground.actions;

import reform.data.sheet.Definition;
import reform.data.sheet.Value;
import reform.data.sheet.expression.ConstantExpression;
import reform.evented.core.EventedSheet;
import reform.identity.IdentifierEmitter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class CreateDefinitionAction extends AbstractAction
{
	private final EventedSheet _eSheet;
	private final IdentifierEmitter _idEmitter;

	public CreateDefinitionAction(EventedSheet eSheet, IdentifierEmitter idEmitter) {
		_eSheet = eSheet;
		_idEmitter = idEmitter;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		_eSheet.addDefinition(new Definition(_idEmitter.emit(), _eSheet.getUniqueNameFor("param", null), new ConstantExpression(new Value(0))));
	}
}
