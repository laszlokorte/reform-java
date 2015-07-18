package reform.stage.tooling.factory;

import reform.identity.Identifier;
import reform.identity.IdentifierEmitter;
import reform.naming.Name;

public class FormFactory<T> {
	public interface Builder<T> {
		T build(Identifier<T> id, Name name);
	}

	private final IdentifierEmitter _idEmitter;
	private final Builder<T> _builder;
	private final String _namePrefix;
	private int _counter = 1;

	public FormFactory(final String name, final IdentifierEmitter idEmitter,
			final Builder<T> builder) {
		_namePrefix = name;
		_idEmitter = idEmitter;
		_builder = builder;
	}

	public T build() {
		return _builder.build(_idEmitter.emit(), new Name(_namePrefix + " "
				+ _counter++));
	}
}
