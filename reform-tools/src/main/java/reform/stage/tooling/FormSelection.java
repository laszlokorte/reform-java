package reform.stage.tooling;

import reform.core.forms.Form;
import reform.identity.Identifier;

import java.util.ArrayList;

public class FormSelection
{
	public interface Listener
	{
		void onSelectionChanged(FormSelection focus);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();
	private Identifier<? extends Form> _form;

	public FormSelection()
	{
		// TODO Auto-generated constructor stub
	}

	public void setSelection(final Identifier<? extends Form> identifier)
	{
		if (_form != identifier)
		{
			_form = identifier;
			for (int i = 0; i < _listeners.size(); i++)
			{
				_listeners.get(i).onSelectionChanged(this);
			}
		}
	}

	public boolean isSelected(final Identifier<? extends Form> identifier)
	{
		return _form != null && _form.equals(identifier);
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public Identifier<? extends Form> getSelected()
	{
		return _form;
	}

	public boolean isSet()
	{
		return _form != null;
	}

	public void reset()
	{
		setSelection(null);
	}
}
