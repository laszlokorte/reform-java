package reform.core.runtime.errors;

import reform.core.forms.Form;
import reform.identity.Identifier;

public class UnknownFormError implements RuntimeError
{
	private final Identifier<? extends Form> _formId;

	public UnknownFormError(Identifier<? extends Form> formId)
	{
		_formId = formId;
	}

	public Identifier<? extends Form> getFormId() {
		return _formId;
	}
	@Override
	public String getMessage()
	{
		return "Unknown form";
	}
}
