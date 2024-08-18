package io.github.et.conopt4j.launcher.streams;

import io.github.et.conopt4j.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class Out extends PrintStream {
    public static final PrintStream OUT=System.out;
    private Out(OutputStream out){
        super(out);
    }

    public static void initialize(){
        System.setOut(new Out(OUT));
        try {
            Logger logger = new Logger();
        } catch (Exception ignored) {}
    }

    @Override
    public void println(){

    }
}
