package io.github.et.conopt4j.launcher.streams;

import io.github.et.conopt4j.exceptions.RepeatedLoggerDeclarationException;
import io.github.et.conopt4j.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class Out extends PrintStream {
    public static final PrintStream OUT=System.out;
    public static Logger logger;
    private Out(OutputStream out){
        super(out);
    }

    public static void initialize() throws RepeatedLoggerDeclarationException {
        System.setOut(new Out(OUT));
        logger=new Logger();
    }

    @Override
    public void println(){
        logger.info();
    }
}
