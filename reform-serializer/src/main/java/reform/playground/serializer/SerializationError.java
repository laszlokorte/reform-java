package reform.playground.serializer;

public class SerializationError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SerializationError(final String reason) {
		super(reason);
	}
}
