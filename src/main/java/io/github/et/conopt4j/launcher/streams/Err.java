package io.github.et.conopt4j.launcher.streams;

import io.github.et.conopt4j.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import static io.github.et.conopt4j.logger.Logger.log;

public class Err extends PrintStream {
    public static final PrintStream ERR=System.err;
    private Err(OutputStream out){
        super(out);
    }

    public static void initialize(){
        System.setErr(new Err(ERR));
    }

    @Override
    public void println(){
        log.error();
    }
}
