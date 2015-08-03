package reform.core.analyzer;

import reform.core.forms.Form;
import reform.core.project.Picture;
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

	void announceDepencency(Identifier<? extends Picture> pictureId);

	Form getForm(Identifier<? extends Form> _formId);
}
