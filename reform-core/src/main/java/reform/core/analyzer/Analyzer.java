package reform.core.analyzer;

import reform.core.forms.Form;
import reform.identity.Identifier;

public interface Analyzer {
	public void begin();

	public void finish();

	public void publish(Analyzable instruction, String label);

	public void publishGroup(Analyzable instruction, String label);

	public void pushScope();

	public void popScope();

	public void announceForm(Form form);

	public Form getForm(Identifier<? extends Form> _formId);
}
