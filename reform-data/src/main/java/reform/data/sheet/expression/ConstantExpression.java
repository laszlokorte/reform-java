package reform.data.sheet.expression;


import reform.data.sheet.DataSet;
import reform.data.sheet.Definition;
import reform.data.sheet.Solver;
import reform.data.sheet.Value;
import reform.identity.Identifier;

import java.util.Collection;

public class ConstantExpression implements Expression {
	private final Value _value;

	public ConstantExpression(Value value) {
		_value = value;
	}

	@Override
	public String asString(boolean paren) {
		return _value.asString();
	}

	@Override
	public String toString() {
		return _value.asString();
	}

	@Override
	public Value getValueFor(DataSet set) {
		return _value;
	}

	@Override
	public void collectDependencies(Collection<ReferenceExpression> dependencies) {

	}

	public Value getValue() {
		return _value;
	}
}
