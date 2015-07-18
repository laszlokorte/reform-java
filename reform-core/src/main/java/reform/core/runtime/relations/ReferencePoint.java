package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface ReferencePoint extends Validatable {
	public double getXValueForRuntime(final Runtime runtime);

	public double getYValueForRuntime(final Runtime runtime);

	public String getDescription(Analyzer analyzer);
}
