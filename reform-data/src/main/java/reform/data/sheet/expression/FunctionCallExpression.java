package reform.data.sheet.expression;


import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

public class FunctionCallExpression implements Expression
{
	private final Calculator.Function _function;
	private final Expression[] _params;

	public FunctionCallExpression(final Calculator.Function name, final Expression...
			params)
	{
		_function = name;
		_params = params;
	}

	@Override
	public String asString(final boolean paren)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < _params.length; i++)
		{
			if (i > 0)
			{
				sb.append(", ");
			}
			sb.append(_params[i].asString(false));
		}
		return _function.name().toLowerCase() + "(" + sb + ")";
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		final Value[] params = new Value[_params.length];
		for (int i = 0; i < _params.length; i++)
		{
			params[i] = _params[i].getValueFor(set);
		}
		return Calculator.apply(_function, params);
	}

	@Override
	public void collectDependencies(final Collection<ReferenceExpression> dependencies)
	{
		for (int i = 0; i < _params.length; i++)
		{
			_params[i].collectDependencies(dependencies);
		}
	}
}
