package io.github.et.conopt4j.threading.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private final ConcurrentHashMap<Parameter<?>, Object> a;

    public Context(ConcurrentHashMap<Parameter<?>, Object> a) {
        this.a = a;
    }
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        for (Map.Entry<Parameter<?>, Object> entry : a.entrySet()) {
            Parameter<?> p = entry.getKey();
            if (p.getName().equals(name)) {
                Parameter<T> typedParam = (Parameter<T>) p;
                return (T) entry.getValue();
            }
        }

        return null;
    }
}
