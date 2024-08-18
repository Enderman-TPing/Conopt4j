package io.github.et.conopt4j.exceptions;

public class RepeatedLoggerDeclarationException extends Exception{
    public RepeatedLoggerDeclarationException(){}
    public RepeatedLoggerDeclarationException(String message){
        super(message);
    }
    public RepeatedLoggerDeclarationException(String message, Throwable cause){
        super(message,cause);
    }
    public RepeatedLoggerDeclarationException(Throwable cause){
        super(cause);
    }
}
