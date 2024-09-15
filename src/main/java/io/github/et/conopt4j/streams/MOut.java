package io.github.et.conopt4j.streams;

import java.io.OutputStream;
import java.io.PrintStream;

public class MOut extends PrintStream{
    public static final PrintStream OriOUT=System.out;
    private MOut(OutputStream out){super(out);}
    public static void initialize(){System.setOut(new MOut(OriOUT));}

    public synchronized static void update0(){
        OriOUT.print("\033[1A\033[K".repeat(LineProcessor.getLength()));
    }
    public synchronized static void update(){
        for (String i:LineProcessor.getProgressBars()) {
            OriOUT.println(i);
        }
        OriOUT.println(LineProcessor.getMonitor());
        OriOUT.print(LineProcessor.getInput());
    }

    //TODO:Bug on: if you print one line that does not change line, there might be mistakes
//    @Override
//    public void print(String s){
//        update0();
//        print(s);
//    }
//    @Override
//    public void print(int x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(long x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(double x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(float x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(char x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(char[] x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(boolean x){print0(String.valueOf(x),info);}
//    @Override
//    public void print(Object x){print0(String.valueOf(x),info);}
//    @Override
//    public void println(){println0("",info);}
//    @Override
//    public void println(String s){ println0(s,info);}
//    @Override
//    public void println(int x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(long x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(double x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(float x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(char x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(char[] x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(boolean x){println0(String.valueOf(x),info);}
//    @Override
//    public void println(Object x){println0(String.valueOf(x),info);}
}
