package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.streams.Out;
import org.fusesource.jansi.AnsiConsole;

public class Launcher {
    public static boolean isAnsiInstalled=false;
    public static void init() {
        if(!isAnsiInstalled){
            AnsiConsole.systemInstall();
            isAnsiInstalled = true;
        }
        Out.initialize();
        Err.initialize();
    }
}
