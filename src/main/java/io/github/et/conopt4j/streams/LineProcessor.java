package io.github.et.conopt4j.streams;

import java.util.ArrayList;

public class LineProcessor {
    private static String currentLine="";
    private static final ArrayList<String> progressBars=new ArrayList<>();
    private static String monitor="";
    private static String input="";//TODO +hint

    public static String getCurrentLine() {
        return currentLine;
    }
    public static int getCurrentLength(){
        return currentLine.length();
    }
    public static void setCurrentLine(String s){
        currentLine=s;
    }

    public static ArrayList<String> getProgressBars() {
        return progressBars;
    }

    public static String getMonitor() {
        return monitor;
    }

    public static void setMonitor(String monitor) {
        LineProcessor.monitor = monitor;
    }
    public static void setInput(String input){
        LineProcessor.input=input;
    }
    public static void appendInput(String a){
        LineProcessor.input+=a;
    }
    public static void refreshInput(){
        input="";
    }
    public static String getInput(){
        return input;
    }


    public static int getLength(){
        int a=1;
        if(!monitor.isEmpty()){
            a+=3;
        }
        a+=progressBars.size();
        return a;
    }
}
