package reform.data.sheet;

import reform.identity.Identifier;

public class Solver
{

	private final DataSet _dataSet;

	public Solver(final DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void evaluate(final Sheet sheet)
	{

		_dataSet.clear();
		sheet.prepareForSolving();

		for (int i = sheet.sortedSize() - 1; i >= 0; i--)
		{
			final Definition def = sheet.sortedGet(i);
			final Identifier<? extends Definition> id = def.getId();
			try
			{
				final Value value = def.getExpression().getValueFor(_dataSet);
				_dataSet.put(id, value);
			} catch (final Calculator.SemanticException e)
			{
				_dataSet.put(id, new Value(0));
				_dataSet.markError(id);
			}
		}

	}


}
