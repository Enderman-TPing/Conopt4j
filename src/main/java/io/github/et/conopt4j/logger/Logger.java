package io.github.et.conopt4j.logger;


import io.github.et.conopt4j.exceptions.LevelNotMatchException;
import io.github.et.conopt4j.exceptions.LoggerNotDeclaredException;
import io.github.et.conopt4j.exceptions.RepeatedLoggerDeclarationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.github.et.conopt4j.launcher.streams.Err.ERR;
import static io.github.et.conopt4j.launcher.streams.Out.OUT;

/**
 * The class Logger is a class for logging as it literally shows. Only one Logger can be used in a certain project. If the Logger is used in different classes, you should call <h4>Logger.getDeclaredLogger</h4> to get the declared logger<br/>
 * For more information, plz see the JavaDoc on the functions
 * @author Enderman-Teleporting
 */
@SuppressWarnings(value={"unused"})
public class Logger{

    /**
     * The levels contain Levels.INFO, Levels.DEBUG and Levels.SEVERE <br/>
     * For Levels.INFO, you can call: info(), error(), fatal() <br/>
     * For Levels.DEBUG, you can call: info(), error(), fatal(), debug(), severe(), fine() <br/>
     * For Levels.SEVERE, you can call: info(), error(), fatal(), severe(), fine() <br/>
     * If you call the functions that does not match the level you have declared, a LevelNotMatchException will be thrown
     * @author Enderman-Teleporting
     */
    public enum Levels{
        INFO,DEBUG,SEVERE
    }
    public static Logger log;
    private static Levels level=Levels.INFO;
    private static final String format="[%s] %s ----------------- %s %n";
    private static String fileOutPut=null;
    public static boolean declared=false;

    private static final SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    /** A constructor with no params
     * @author Enderman-Teleporting
     * @throws RepeatedLoggerDeclarationException -> Called when a Logger has already been declared
    */
    public Logger() throws RepeatedLoggerDeclarationException {
        try {
            if (Logger.fileOutPut.isEmpty()) {
                Logger.fileOutPut = null;
            }
        }
        catch (Exception ignored){}
        if(!Logger.declared){
            Logger.declared=true;
            try {
                log=Logger.getDeclaredLogger();
            } catch (LoggerNotDeclaredException ignored) {}
        }else{
            throw new RepeatedLoggerDeclarationException("One Logger has already been declared");
        }

    }

    /**
     * A constructor with params <br/>
     * @author Enderman-Teleporting
     * @param level (String) -> Logger level (containing Levels.INFO, Levels.DEBUG and Levels.SEVERE)
     * @param fileOutput (String) -> The path for a log file that you may want to record the logs in
     * @throws RepeatedLoggerDeclarationException -> Called when a Logger has already been declared
     */
    public Logger(Levels level,String fileOutput) throws RepeatedLoggerDeclarationException {
        try {
            if (Logger.fileOutPut.isEmpty()) {
                Logger.fileOutPut = null;
            }
        }
        catch (Exception ignored){}
        if(!Logger.declared) {
            Logger.level = level;
            fileOutPut = fileOutput;
            Logger.declared = true;
        }else{
            throw new RepeatedLoggerDeclarationException("One Logger has already been declared");
        }
    }

    /**
     * A constructor with params <br/>
     * @author Enderman-Teleporting
     * @param level (String) -> Logger level (containing Levels.INFO, Levels.DEBUG and Levels.SEVERE)
     * @throws RepeatedLoggerDeclarationException -> Called when a Logger has already been declared
     */
    public Logger(Levels level) throws RepeatedLoggerDeclarationException {
        try {
            if (Logger.fileOutPut.isEmpty()) {
                Logger.fileOutPut = null;
            }
        }
        catch (Exception ignored){}
        if(!Logger.declared) {
            Logger.level = level;
            Logger.declared = true;
        }else{
            throw new RepeatedLoggerDeclarationException("One Logger has already been declared");
        }
    }

    /**
     * A function to get the Logger that you have already declared
     * @author Enderman-Teleporting
     * @return The Logger that you have declared in other classes
     * @throws LoggerNotDeclaredException -> Called when no Logger has been declared
     */
    public static Logger getDeclaredLogger() throws LoggerNotDeclaredException {
        try {
            if (Logger.fileOutPut.isEmpty()) {
                Logger.fileOutPut = null;
            }
        }
        catch (Exception ignored){}
        if(!Logger.declared){
           throw new LoggerNotDeclaredException("No logger has been declared. You need to declare one first");
        }else {
            Logger.declared = false;
            try{return new Logger(Logger.level, Logger.fileOutPut);}catch(Exception ignored){return null;}
        }
    }

    /**
     * A function to set logger level <br/>
     * The default value of logger level is Logger.INFO
     * @author Enderman-Teleporting
     * @param level (Logger.Level) -> Logger level
     */
    public void setLevel(Levels level){Logger.level =level;}

    /**
     * A function to set the log output file <br/>
     * The default value of the output file is null
     * @author Enderman-Teleporting
     * @param outputFile (String) -> File path
     */
    public void setFileOutput(String outputFile){fileOutPut=outputFile;}

    /**
     * The [INFO] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    public void info(String content, Object...f){
        String str;
        String[] contents;
        if (content.contains("\n")){
            contents=content.split("\n");
        }else{
            contents= new String[]{content};
        }
        Date date=new Date(System.currentTimeMillis());
        for (String cnt:contents){
            String s=String.format(cnt, f);
            OUT.printf(format,"INFO",fmt.format(date),s);
            if(fileOutPut!=null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"INFO",fmt.format(date),s);
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * The [ERROR] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    public void error(String content,Object ... f){
        String str;
        String[] contents;
        if (content.contains("\n")){
            contents=content.split("\n");
        }else{
            contents= new String[]{content};
        }
        Date date=new Date(System.currentTimeMillis());
        for (String cnt:contents) {
            String s=String.format(cnt, f);
            ERR.printf(format, "ERROR",fmt.format(date) , s);
            if(fileOutPut!=null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"ERROR",fmt.format(date),s);
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The [FATAL] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    public void fatal(String content,Object ... f){
        String str;
        String[] contents;
        if (content.contains("\n")){
            contents=content.split("\n");
        }else{
            contents= new String[]{content};
        }
        Date date=new Date(System.currentTimeMillis());
        for (String cnt:contents) {
            String s=String.format(cnt, f);
            ERR.printf(format, "FATAL",fmt.format(date) , s);
            if(fileOutPut!=null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"FATAL",fmt.format(date),s);
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The [DEBUG] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG
     */
    public void debug(String content, Object ...f) throws LevelNotMatchException {
        if(Logger.level.equals(Levels.DEBUG)){
            String str;
            String[] contents;
            if (content.contains("\n")){
                contents=content.split("\n");
            }else{
                contents= new String[]{content};
            }
            Date date=new Date(System.currentTimeMillis());
            for (String cnt:contents){
                String s=String.format(cnt, f);
                OUT.printf(format,"DEBUG",fmt.format(date),s);
                if(fileOutPut!=null){
                    File file=new File(fileOutPut);
                    if(!file.exists()){
                        try {
                            boolean bo=file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    str=String.format(format,"DEBUG",fmt.format(date),s);
                    try {
                        FileOutputStream bw=new FileOutputStream(file,true);
                        bw.write(str.getBytes());
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }

    /**
     * The [SEVERE] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG or Levels.SEVERE
     */
    public void severe(String content,Object...f) throws LevelNotMatchException {
        if(Logger.level.equals(Levels.DEBUG)||Logger.level.equals(Levels.SEVERE)){
            String str;
            String[] contents;
            if (content.contains("\n")){
                contents=content.split("\n");
            }else{
                contents= new String[]{content};
            }
            Date date=new Date(System.currentTimeMillis());
            for (String cnt:contents){
                String s=String.format(cnt, f);
                ERR.printf(format,"SEVERE",fmt.format(date),s);
                if(fileOutPut!=null){
                    File file=new File(fileOutPut);
                    if(!file.exists()){
                        try {
                            boolean bo=file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    str=String.format(format,"SEVERE",fmt.format(date),s);
                    try {
                        FileOutputStream bw=new FileOutputStream(file,true);
                        bw.write(str.getBytes());
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }

    /**
     * The [FINE] log output <br/>
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG or Levels.SEVERE
     */
    public void fine(String content,Object...f) throws LevelNotMatchException {
        String str;
        if (Logger.level.equals(Levels.SEVERE)||Logger.level.equals(Levels.DEBUG)){
            String[] contents;
            if (content.contains("\n")){
                contents=content.split("\n");
            }else{
                contents= new String[]{content};
            }
            Date date=new Date(System.currentTimeMillis());
            for (String cnt:contents){
                String s=String.format(cnt, f);
                OUT.printf(format,"FINE",fmt.format(date),s);
                if(fileOutPut!=null){
                    File file=new File(fileOutPut);
                    if(!file.exists()){
                        try {
                            boolean bo=file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    str=String.format(format,"FINE",fmt.format(date),s);
                    try {
                        FileOutputStream bw=new FileOutputStream(file,true);
                        bw.write(str.getBytes());
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }
    /**
     * The blank [INFO] log output <br/>
     * @author Enderman-Teleporting
     */
    public void info(){
        String str;
        Date date=new Date(System.currentTimeMillis());
        OUT.printf(format, "INFO",fmt.format(date),"");
        if(fileOutPut != null){
            File file=new File(fileOutPut);
            if(!file.exists()){
                try {
                    boolean bo=file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            str=String.format(format,"INFO",fmt.format(date),"");
            try {
                FileOutputStream bw=new FileOutputStream(file,true);
                bw.write(str.getBytes());
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The blank [ERROR] log output <br/>
     * @author Enderman-Teleporting
     */
    public void error(){
        String str;
        Date date=new Date(System.currentTimeMillis());
        ERR.printf(format, "ERROR",fmt.format(date),"");
        if(fileOutPut != null){
            File file=new File(fileOutPut);
            if(!file.exists()){
                try {
                    boolean bo=file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            str=String.format(format,"ERROR",fmt.format(date),"");
            try {
                FileOutputStream bw=new FileOutputStream(file,true);
                bw.write(str.getBytes());
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The blank [FATAL] log output <br/>
     * @author Enderman-Teleporting
     */
    public void fatal(){
        String str;
        Date date=new Date(System.currentTimeMillis());
        ERR.printf(format, "FATAL",fmt.format(date),"");
        if(fileOutPut != null){
            File file=new File(fileOutPut);
            if(!file.exists()){
                try {
                    boolean bo=file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            str=String.format(format,"FATAL",fmt.format(date),"");
            try {
                FileOutputStream bw=new FileOutputStream(file,true);
                bw.write(str.getBytes());
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The blank [FINE] log output <br/>
     * @author Enderman-Teleporting
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG or Levels.SEVERE
     */
    public void fine() throws LevelNotMatchException {
        if (Logger.level.equals(Levels.SEVERE)||Logger.level.equals(Levels.DEBUG)){
            String str;
            Date date=new Date(System.currentTimeMillis());
            OUT.printf(format,"FINE",fmt.format(date),"");
            if(fileOutPut != null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"FINE",fmt.format(date),"");
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }

    /**
     * The blank [SEVERE] log output <br/>
     * @author Enderman-Teleporting
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG or Levels.SEVERE
     */
    public void severe() throws LevelNotMatchException {
        if (Logger.level.equals(Levels.SEVERE)||Logger.level.equals(Levels.DEBUG)){
            String str;
            Date date=new Date(System.currentTimeMillis());
            ERR.printf(format,"SEVERE",fmt.format(date),"");
            if(fileOutPut != null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"SEVERE",fmt.format(date),"");
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }

    /**
     * The blank [DEBUG] log output <br/>
     * @author Enderman-Teleporting
     * @throws LevelNotMatchException -> Called when your level is not Levels.DEBUG
     */
    public void debug() throws LevelNotMatchException {
        if (Logger.level.equals(Levels.DEBUG)){
            String str;
            Date date=new Date(System.currentTimeMillis());
            OUT.printf(format,"DEBUG",fmt.format(date),"");
            if(fileOutPut != null){
                File file=new File(fileOutPut);
                if(!file.exists()){
                    try {
                        boolean bo=file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                str=String.format(format,"DEBUG",fmt.format(date),"");
                try {
                    FileOutputStream bw=new FileOutputStream(file,true);
                    bw.write(str.getBytes());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new LevelNotMatchException("The current level is "+level+", which is lower than needed");
        }
    }



    @Override
    public String toString(){
        return (level+"\t"+fileOutPut);
    }

}