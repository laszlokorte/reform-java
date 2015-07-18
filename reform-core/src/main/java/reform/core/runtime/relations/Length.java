package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface Length extends Validatable {
	double getValueForRuntime(final Runtime runtime);

}
