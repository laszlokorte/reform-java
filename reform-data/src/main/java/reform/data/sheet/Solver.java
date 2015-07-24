package reform.data.sheet;

import reform.identity.Identifier;

import javax.xml.crypto.Data;
import java.util.HashMap;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class Solver
{

	private final DataSet _dataSet;

	public Solver(DataSet dataSet) {
		_dataSet = dataSet;
	}

	public void evaluate(Sheet sheet) {

		_dataSet.clear();
		sheet.prepareForSolving();

		for(int i=sheet.sortedSize()-1;i>=0;i--) {
			Definition def = sheet.sortedGet(i);
			Identifier<? extends Definition> id = def.getId();
			try {
				Value value = def.getExpression().getValueFor(_dataSet);
				_dataSet.put(id, value);
			} catch(Calculator.SemanticException e) {
				_dataSet.put(id, new Value(0));
				_dataSet.markError(id);
			}
		}

	}


}
