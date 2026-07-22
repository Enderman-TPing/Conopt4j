package io.github.et.conopt4j.streams;

import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.conopt4j.logger.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

public class OldOut extends PrintStream {
    public OldOut(OutputStream out) {
        super(out);
    }

    public void print() {
        print0("");
    }

    @Override
    public void print(String s) {
        print0(s);
    }

    @Override
    public void print(int x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(long x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(double x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(float x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(char x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(char[] x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(boolean x) {
        print0(String.valueOf(x));
    }

    @Override
    public void print(Object x) {
        print0(String.valueOf(x));
    }

    @Override
    public void println() {
        println0("");
    }

    @Override
    public void println(String s) {
        println0(s);
    }

    @Override
    public void println(int x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(long x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(double x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(char x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(char[] x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(boolean x) {
        println0(String.valueOf(x));
    }

    @Override
    public void println(Object x) {
        println0(String.valueOf(x));
    }

    public void printf() {
        print0("");
    }

    @Override
    public PrintStream printf(String s, Object... f) {
        printf0(s, f);
        return this;
    }


    private void println0(String x) {
        Launcher.READER.printAbove(x);
        Logger.getLogHistory().add(x);
    }

    private void print0(String x) {
        synchronized (Launcher.getStatusList()) {
            if (x.endsWith("\n")) {
                println0(x);
                return;
            }
            String[] contents = x.split("\\r\\n|(?<!\\r\\n)\\n(?!\\r\\n)|(?<!\\r\\n|\\n)\\r(?!\\r\\n|\\n)");
            if (contents.length > 1) {
                for (int i = 0; i < contents.length - 1; i++) {
                    println0(contents[i]);
                }
                Out.buffer = contents[contents.length - 1];
            } else {
                Out.buffer = Out.buffer + contents[0];
            }
        }
    }
    private void printf0(String x, Object... f) {
        String a = String.format(x, f);
        print(a.replace("\r\n", "\n"));
    }

}

