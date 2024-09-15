package io.github.et.conopt4j.exceptions;

public class DuplicateMonitorException extends Exception{
    public DuplicateMonitorException(){}
    public DuplicateMonitorException(String message){
        super(message);
    }
    public DuplicateMonitorException(String message, Throwable cause){
        super(message,cause);
    }
    public DuplicateMonitorException(Throwable cause){
        super(cause);
    }
}
