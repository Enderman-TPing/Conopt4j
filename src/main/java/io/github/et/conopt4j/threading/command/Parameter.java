package io.github.et.conopt4j.threading.command;

public class Parameter<T> {
    private final String name;
    private final Class<T> type;
    private final Parser<T> parser;
    @SuppressWarnings("unchecked")
    public Parameter(String name, Type type) {
        this.name = name;
        this.type = (Class<T>) type.getType();
        this.parser = (Parser<T>) switch (type){
            case INTEGER -> Parsers.INTEGER;
            case STRING -> Parsers.STRING;
            case BOOLEAN -> Parsers.BOOLEAN;
            case LONG -> Parsers.LONG;
            case DOUBLE -> Parsers.DOUBLE;
        };
    }

    public String getName() { return name; }
    public Class<T> getType() { return type; }
    public Parser<T> getParser() { return parser; }
}
