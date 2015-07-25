package reform.core.forms.relations;

import reform.core.analyzer.Analyzer;
import reform.core.forms.Form;
import reform.core.runtime.Runtime;
import reform.core.runtime.relations.ReferencePoint;
import reform.identity.Identifiable;
import reform.identity.Identifier;
import reform.identity.IdentityToken;
import reform.naming.Name;

public class ExposedPoint implements ReferencePoint, Identifiable<ExposedPoint>
{

	private final Identifier<ExposedPoint> _id;
	private final ReferencePoint _point;
	private final Name _name;
	public ExposedPoint(final ReferencePoint point, final Name name, final
	ExposedPointToken<? extends Form> id)
	{
		_point = point;
		_name = name;
		_id = new Identifier<>(id);
	}

	@Override
	public double getXValueForRuntime(final Runtime runtime)
	{
		return _point.getXValueForRuntime(runtime);
	}

	@Override
	public double getYValueForRuntime(final Runtime runtime)
	{
		return _point.getYValueForRuntime(runtime);
	}

	@Override
	public String getDescription(final Analyzer analyzer)
	{
		return _name.getValue();
	}

	@Override
	public Identifier<? extends ExposedPoint> getId()
	{
		return _id;
	}

	@Override
	public boolean isValidFor(final Runtime runtime)
	{
		return _point.isValidFor(runtime);
	}

	public interface ExposedPointToken<T> extends IdentityToken
	{

	}
}
