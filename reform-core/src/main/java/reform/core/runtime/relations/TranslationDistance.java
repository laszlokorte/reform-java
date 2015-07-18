package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface TranslationDistance extends Validatable {

	public double getXValueForRuntime(final Runtime runtime);

	public double getYValueForRuntime(final Runtime runtime);

	public String getDescription(Analyzer analyzer);

	public boolean isDegenerated();
}
