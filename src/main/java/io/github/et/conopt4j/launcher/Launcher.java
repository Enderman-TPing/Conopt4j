package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.exceptions.RepeatedLoggerDeclarationException;
import io.github.et.conopt4j.launcher.streams.Err;
import io.github.et.conopt4j.launcher.streams.Out;
import io.github.et.conopt4j.logger.Logger;

public class Launcher {
    public static Logger launch() throws RepeatedLoggerDeclarationException {
        Out.initialize();
        Err.initialize();
        try {
            return Logger.getDeclaredLogger();
        }catch (Exception ignored){
            return null;
        }
    }
}
