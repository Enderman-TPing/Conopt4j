package io.github.et.conopt4j.streams;

public class LineProcessor {
    private static String currentLine="";

    public static String getCurrentLine() {
        return currentLine;
    }
    public static int getCurrentLength(){
        return currentLine.length();
    }
    public static void setCurrentLine(String s){
        currentLine=s;
    }
}
