package reform.core.analyzer;

import reform.core.forms.Form;
import reform.identity.Identifier;

public interface Analyzer
{
	void begin();

	void finish();

	void publish(Analyzable instruction, String label);

	void publishGroup(Analyzable instruction, String label);

	void pushScope();

	void popScope();

	void announceForm(Form form);

	Form getForm(Identifier<? extends Form> _formId);
}
