package io.metacloud;


public class Options<T> {

    public static final Options<Integer> TIMEOUT = new Options<>(-1);
    public static final Options<Integer> BUFFER_SIZE = new Options<>(1024);
    public static final Options<Integer> TEST = new Options<>(1024);
    protected T value;

    protected Options(T value) {
        this.value = value;
    }

    protected T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    protected void setValue(Object value) {
        this.value = (T) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
