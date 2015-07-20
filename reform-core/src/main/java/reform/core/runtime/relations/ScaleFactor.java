package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface ScaleFactor extends Validatable
{
	double getValueForRuntime(final Runtime runtime);

	String getDescription(final Analyzer analyzer);

	boolean isDegenerated();
}
