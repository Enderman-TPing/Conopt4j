package io.github.et.conopt4j.threading.command;

@FunctionalInterface
public interface Parser<T> {
    T parse(String input);
}