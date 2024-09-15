package io.github.et.conopt4j.exceptions;

public class LevelNotMatchException extends Exception{
    public LevelNotMatchException(){}
    public LevelNotMatchException(String message){
        super(message);
    }
    public LevelNotMatchException(String message,Throwable cause){
        super(message,cause);
    }
    public LevelNotMatchException(Throwable cause){
        super(cause);
    }

}
