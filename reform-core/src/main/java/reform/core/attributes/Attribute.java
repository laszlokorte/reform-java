package reform.core.attributes;

public class Attribute<E> {
    private final String _name;
    private final Class<E> _type;
    private E _value;

    public Attribute(String name, Class<E> type, E initialValue) {
        _name = name;
        _type = type;
        _value = initialValue;
    }

    public String getName() {
        return _name;
    }

    public Class<E> getType() {
        return _type;
    }

    public E getValue() {
        return _value;
    }

    public void setValue(final E newValue) {
        _value = newValue;
    }
}
