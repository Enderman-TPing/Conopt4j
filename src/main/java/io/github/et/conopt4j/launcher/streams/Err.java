package io.github.et.conopt4j.launcher.streams;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static io.github.et.conopt4j.launcher.streams.Out.logger;
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
        logger.error();
    }

    @Override
    public void println(String s){
        logger.error(s);
    }
    @Override
    public void println(int x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(long x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(double x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(float x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(char x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(char[] x){
        logger.error(Arrays.toString(x));
    }
    @Override
    public void println(boolean x){
        logger.error(String.valueOf(x));
    }
    @Override
    public void println(Object x){
        logger.error(x.toString());
    }

}
