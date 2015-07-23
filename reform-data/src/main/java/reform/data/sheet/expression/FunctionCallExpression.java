package reform.data.sheet.expression;


import reform.data.sheet.*;
import reform.identity.Identifier;

import java.util.Collection;

/**
 * Created by laszlokorte on 21.07.15.
 */
public class FunctionCallExpression implements Expression {
	private final Calculator.Function _function;
	private final Expression[] _params;

	public FunctionCallExpression(Calculator.Function name, Expression... params) {
		_function = name;
		_params = params;
	}

	@Override
	public String asString(boolean paren) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<_params.length;i++) {
			if(i>0) {
				sb.append(", ");
			}
			sb.append(_params[i].asString(false));
		}
		return _function.name().toLowerCase() + "(" + sb + ")";
	}

	@Override
	public Value getValueFor(DataSet set) {
		Value[] params = new Value[_params.length];
		for(int i=0;i<_params.length;i++) {
			params[i] = _params[i].getValueFor(set);
		}
		return Calculator.apply(_function, params);
	}

	@Override
	public void collectDependencies(Collection<ReferenceExpression> dependencies) {
		for(int i=0;i<_params.length;i++) {
			_params[i].collectDependencies(dependencies);
		}
	}
}
