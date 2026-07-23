package io.github.et.conopt4j.threading.command;

public class Parsers {
    public static final Parser<String> STRING = s -> s;

    public static final Parser<Integer> INTEGER = s -> {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }

    };

    public static final Parser<Boolean> BOOLEAN = s -> {
        if ("true".equalsIgnoreCase(s)) return true;
        if ("false".equalsIgnoreCase(s)) return false;
        return null;
    };

    public static final Parser<Long> LONG = s -> {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    };
    public static final Parser<Double> DOUBLE = s -> {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    };
}

