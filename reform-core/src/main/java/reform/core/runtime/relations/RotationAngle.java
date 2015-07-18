package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface RotationAngle extends Validatable {

	public double getValueForRuntime(final Runtime runtime);

	public String getDescription(Analyzer analyzer);

}
