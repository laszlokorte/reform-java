package reform.data.sheet.expression;


import reform.data.sheet.Calculator;
import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

public class SubtractionExpression extends BinaryExpressionBase implements Expression
{

	public SubtractionExpression(final Expression lhs, final Expression rhs)
	{
		super(lhs, rhs);
	}

	@Override
	public String asString(final boolean parens)
	{
		final String s = _lhs.asString(false) + " - " + _rhs.asString(false);
		if (parens)
		{
			return "(" + s + ")";
		}
		else
		{
			return s;
		}
	}

	@Override
	public Value getValueFor(final DataSet set)
	{
		return Calculator.subtract(_lhs.getValueFor(set), _rhs.getValueFor(set));
	}
}
