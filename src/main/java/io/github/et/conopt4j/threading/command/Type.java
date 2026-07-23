package io.github.et.conopt4j.threading.command;

public enum Type {
    INTEGER(Integer.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    LONG(Long.class),
    DOUBLE(Double.class);
    private Class<?> a;
    Type(Class<?> a) {
        this.a = a;
    }
    public Class<?> getType() {
        return a;
    }
}
