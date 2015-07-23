package reform.core.runtime.relations;

import reform.core.analyzer.Analyzer;
import reform.core.runtime.Runtime;
import reform.core.runtime.Validatable;

public interface RotationAngle extends Validatable
{

	double getValueForRuntime(final Runtime runtime);

	String getDescription(Analyzer analyzer);

	boolean isDegenerated();

}
