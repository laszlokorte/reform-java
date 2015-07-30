package reform.stage.tooling.factory;

import reform.identity.Identifier;
import reform.identity.IdentifierEmitter;
import reform.naming.Name;

public class FormFactory<T>
{
	private final IdentifierEmitter _idEmitter;
	private final Builder<T> _builder;
	private final Configurator<T> _configurator;
	private final String _namePrefix;
	private int _counter = 1;
	public FormFactory(final String name, final IdentifierEmitter idEmitter, final
	Builder<T> builder)
	{
		this(name, idEmitter, builder, (f)-> {});
	}

	public FormFactory(final String name, final IdentifierEmitter idEmitter, final
	Builder<T> builder, Configurator<T> conf)
	{
		_namePrefix = name;
		_idEmitter = idEmitter;
		_builder = builder;
		_configurator = conf;
	}

	public T build()
	{
		T e =  _builder.build(_idEmitter.emit(),
		                      new Name(_namePrefix + " " + _counter++));
		_configurator.configure(e);
		return e;
	}

	public interface Builder<T>
	{
		T build(Identifier<T> id, Name name);
	}


	public interface Configurator<T>
	{
		void configure(T elment);
	}
}
