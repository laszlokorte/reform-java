package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface TranslationDistance extends Validatable {

	double getXValueForRuntime(final Runtime runtime);

	double getYValueForRuntime(final Runtime runtime);

	String getDescription(Analyzer analyzer);

	boolean isDegenerated();
}
