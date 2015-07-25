package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Value;

import java.util.Collection;

public interface Expression
{
	String asString(boolean parens);

	Value getValueFor(DataSet set);

	void collectDependencies(Collection<ReferenceExpression> dependencies);
}
