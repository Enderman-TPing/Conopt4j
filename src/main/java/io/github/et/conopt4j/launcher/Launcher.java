package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.streams.MErr;
import io.github.et.conopt4j.streams.MOut;
import io.github.et.conopt4j.streams.Out;
import org.fusesource.jansi.AnsiConsole;

import java.io.InputStream;

public class Launcher {
    public static boolean isAnsiInstalled=false;
    public static void init(InputStream in) {
        if(!isAnsiInstalled){
            AnsiConsole.systemInstall();
            isAnsiInstalled = true;
        }
        PropertyLoader.loadProperties(in);
        MOut.initialize();
        MErr.initialize();
        Out.initialize();
        Err.initialize();
    }
    public static void init_debug(InputStream in) {
        PropertyLoader.loadProperties(in);
        MOut.initialize();
        MErr.initialize();
        Out.initialize();
        Err.initialize();
    }
}
