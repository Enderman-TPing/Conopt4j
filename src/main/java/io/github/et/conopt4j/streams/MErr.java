package io.github.et.conopt4j.streams;

import java.io.OutputStream;
import java.io.PrintStream;

public class MErr extends PrintStream {
    public static final PrintStream OriERR=System.err;
    private MErr(OutputStream out){super(out);}
    public static void initialize(){System.setErr(new MErr(OriERR));}


}
