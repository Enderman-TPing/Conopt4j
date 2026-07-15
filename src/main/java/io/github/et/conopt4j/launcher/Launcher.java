package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.streams.MErr;
import io.github.et.conopt4j.streams.MOut;
import io.github.et.conopt4j.streams.Out;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Launcher {
    public static Terminal TERMINAL;
    public static LineReader READER;
    public static boolean UseMonitor=false;
    public static void init(InputStream in) throws IOException {
        PropertyLoader.loadProperties(in);
        TERMINAL = TerminalBuilder.terminal();
        READER = LineReaderBuilder.builder()
                .terminal(TERMINAL)
                .build();
        MOut.initialize();
        MErr.initialize();
        Out.initialize();
        Err.initialize();
    }
}
