package io.github.et.conopt4j.streams;

import io.github.et.conopt4j.launcher.PropertyLoader;
import io.github.et.conopt4j.logger.Logger;
import io.github.et.conopt4j.style.Color;
import io.github.et.conopt4j.style.Style;
import io.github.et.conopt4j.style.Style_headless;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static io.github.et.conopt4j.logger.Logger.getLogger;

public class Err extends PrintStream {
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private final Color error = PropertyLoader.getError();
    public static final PrintStream ERR=System.err;
    private static final Logger log=getLogger();
    private static final String format = PropertyLoader.getFormat().toString();
    private static final String format_headless= PropertyLoader.getFormat_headless().toString();
    private static final String fileOutPut = PropertyLoader.getLogOutPut();

    private Err(OutputStream out){
        super(out);
    }

    public static void initialize(){
        System.setErr(new Err(ERR));
    }


    public void print(){print0("",error);}
    @Override
    public void print(String s){ print0(s,error);}
    @Override
    public void print(int x){print0(String.valueOf(x),error);}
    @Override
    public void print(long x){print0(String.valueOf(x),error);}
    @Override
    public void print(double x){print0(String.valueOf(x),error);}
    @Override
    public void print(float x){print0(String.valueOf(x),error);}
    @Override
    public void print(char x){print0(String.valueOf(x),error);}
    @Override
    public void print(char[] x){print0(String.valueOf(x),error);}
    @Override
    public void print(boolean x){print0(String.valueOf(x),error);}
    @Override
    public void print(Object x){print0(String.valueOf(x),error);}
    @Override
    public void println(){println0("",error);}
    @Override
    public void println(String s){ println0(s,error);}
    @Override
    public void println(int x){println0(String.valueOf(x),error);}
    @Override
    public void println(long x){println0(String.valueOf(x),error);}
    @Override
    public void println(double x){println0(String.valueOf(x),error);}
    @Override
    public void println(float x){println0(String.valueOf(x),error);}
    @Override
    public void println(char x){println0(String.valueOf(x),error);}
    @Override
    public void println(char[] x){println0(String.valueOf(x),error);}
    @Override
    public void println(boolean x){println0(String.valueOf(x),error);}
    @Override
    public void println(Object x){println0(String.valueOf(x),error);}

    public void printf(){print0("",error);}
    @Override
    public PrintStream printf(String s, Object...f){
        printf0(s,f);
        return this;
    }


    private void println0(String x, Color color){
        if(LineProcessor.getCurrentLine().isEmpty()){
            log.error(x);
        }else{
            String[] contents = x.contains("\n") ? x.split("\n") : new String[]{x};
            StringBuilder sb = new StringBuilder();

            for (String cnt : contents) {
                if(!LineProcessor.getCurrentLine().isEmpty()) {
                    String output = String.format(format_headless, color.toString(), cnt,"\n");
                    sb.append(output);
                    Err.ERR.printf(output);
                }else{
                    log.error(cnt);
                }
            }

            if (fileOutPut != null) {
                writeToFile(sb.toString());
            }
        }
        LineProcessor.setCurrentLine("");
    }
    private void print0(String x, Color color) {

        String[] contents = x.contains("\n") ? x.split("\n") : new String[]{x};
        StringBuilder sb = new StringBuilder();
        if (contents.length > 1) {
            for (int i = 0; i < contents.length - 1; i++) {
                println(contents[i]);
            }
            String output = String.format(format, color.toString(), "ERROR", fmt.format(new Date()), caller(), contents[contents.length - 1], "");
            ERR.print(output);
            if (fileOutPut != null) {
                writeToFile(output);
            }
            LineProcessor.setCurrentLine(output);
        } else {
            if(LineProcessor.getCurrentLine().isEmpty()){
                String output = String.format(format, color.toString(), "ERROR", fmt.format(new Date()), caller(), contents[contents.length - 1], "");
                ERR.print(output);
                LineProcessor.setCurrentLine(output);
                if (fileOutPut != null) {
                    writeToFile(output);
                }
            }else{
                String output = String.format(format_headless, color.toString(), x, "");
                ERR.print(output);
                LineProcessor.setCurrentLine(output);
                if (fileOutPut != null) {
                    writeToFile(sb.toString());
                }
            }

        }
    }

    private void printf0(String x,Object...f){
        String a=String.format(x, f);
        print(a.replace("\r\n","\n"));
    }

    private String caller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for(StackTraceElement i :stackTrace){
            String a=i.getClassName();
            if(!(a.startsWith("io.github.et.conopt4j")||a.startsWith("java"))){
                return a;
            }
        }
        return stackTrace[4].getClassName();
    }

    private void writeToFile(String content) {
        File file = new File(fileOutPut);
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Failed to create file: " + fileOutPut);
            }
            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                fos.write(content.replace("\033[31m","")
                        .replace("\033[32m","")
                        .replace("\033[33m","")
                        .replace("\033[34m","")
                        .replace("\033[35m","")
                        .replace("\033[36m","")
                        .replace("\033[37m","")
                        .replace("\033[0m","")
                        .getBytes());
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
