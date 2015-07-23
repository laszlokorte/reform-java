package reform.data.sheet.expression;

import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.Value;
import reform.identity.Identifier;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface Expression
{
	String asString(boolean parens);

	Value getValueFor(DataSet set);

	void collectDependencies(Collection<ReferenceExpression> dependencies);
}
