package io.github.et.conopt4j.logger;

public enum Level {
    INFO("Level.INFO"), DEBUG("Level.DEBUG"), FINE("Level.FINE");
    private final String name;
    Level(String s) {
        this.name = s;
    }
    @Override
    public String toString() {
        return name;
    }
}
