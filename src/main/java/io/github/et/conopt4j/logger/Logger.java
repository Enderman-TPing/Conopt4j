package io.github.et.conopt4j.logger;

import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.conopt4j.launcher.PropertyLoader;
import io.github.et.conopt4j.streams.Out;
import io.github.et.conopt4j.style.Color;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * The class Logger is a class for logging as it literally shows. Only one Logger can be used in a certain project. If the Logger is used in different classes, you should call <h4>Logger.getDeclaredLogger</h4> to get the declared logger
 *
 * For more information, plz see the Javadoc on the functions
 * @author Enderman-Teleporting
 */
@SuppressWarnings("unused")
public class Logger {
    private static Level level = PropertyLoader.getLevel();
    private static String fileOutPut = PropertyLoader.getLogOutPut();
    private static SimpleDateFormat fmt = new SimpleDateFormat("MM-dd HH:mm:ss");

    private static Color info = PropertyLoader.getInfo();
    private static Color warn = PropertyLoader.getWarn();
    private static Color error = PropertyLoader.getError();
    private static Color fatal = PropertyLoader.getFatal();
    private static Color debug = PropertyLoader.getDebug();
    private static Color fine = PropertyLoader.getFine();
    private static Color severe = PropertyLoader.getSevere();

    /**
     * The [ INFO ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void info(String content, Object... f) {
        log(content, info, "INFO", f);
    }

    /**
     * The [ WARN ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void warn(String content, Object... f) {
        log(content, warn, "WARN", f);
    }

    /**
     * The [ ERROR ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void error(String content, Object... f) {
        log(content, error, "ERROR", f);
    }

    /**
     * The [ FATAL ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void fatal(String content, Object... f) {
        log(content, fatal, "FATAL", f);
    }

    /**
     * The [ DEBUG ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void debug(String content, Object... f) {
        if (level != Level.DEBUG) {
            return;
        }
        log(content, debug, "DEBUG", f);
    }

    /**
     * The [ FINE ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void fine(String content, Object... f) {
        if (level != Level.DEBUG && level != Level.FINE) {
            return;
        }
        log(content, fine, "FINE", f);
    }

    /**
     * The [ SEVERE ] log output
     *
     * @param content (String) -> Log content
     * @param f       (Object...) -> Log format
     * @author Enderman-Teleporting
     */
    public static void severe(String content, Object... f) {
        if (level != Level.DEBUG && level != Level.FINE) {
            return;
        }
        log(content, severe, "SEVERE", f);
    }

    /**
     * The blank [ INFO ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void info() {
        info("");
    }

    /**
     * The blank [ WARN ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void warn() {
        warn("");
    }

    /**
     * The blank [ ERROR ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void error() {
        error("");
    }

    /**
     * The blank [ SEVERE ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void severe() {
        severe("");
    }

    /**
     * The blank [ FINE ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void fine() {
        fine("");
    }

    /**
     * The blank [ DEBUG] log output
     *
     * @author Enderman-Teleporting
     */
    public static void debug() {
        debug("");
    }

    /**
     * The blank [ FATAL ] log output
     *
     * @author Enderman-Teleporting
     */
    public static void fatal() {
        fatal("");
    }

    private static void log(String content, Color color, String logLevel, Object... f) {
        synchronized (Launcher.getStatusList()) {
            String content0 = String.format(content, f);
            String[] contents = content0.contains("\n") ? (content0.split("\n")) : new String[]{content0};
            if (Out.buffer.equals("")) {
                Date date = new Date();
                StringBuilder all = new StringBuilder();
                for (String cnt : contents) {
                    AttributedStringBuilder sb = new AttributedStringBuilder();
                    sb.append("[");
                    sb.style(color.getStyle());
                    sb.append(logLevel);
                    sb.style(AttributedStyle.DEFAULT);
                    sb.append("] ");
                    if (PropertyLoader.isUseDate()) {
                        sb.append(fmt.format(date));
                        sb.append(" ");
                    }
                    if (PropertyLoader.isUseTrace()) {
                        sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
                        sb.append("--");
                        sb.append(caller());
                        sb.append("--");
                        sb.style(AttributedStyle.DEFAULT);
                    }
                    sb.append(" ");
                    sb.append(cnt);
                    all.append(sb.toString()).append("\n");
                    logHistory.add(sb.toString());
                    Launcher.READER.printAbove(sb.toAttributedString());
                }
                if (fileOutPut != null) {
                    writeToFile(all.toString());
                }
            } else {
                contents[0] = Out.buffer + contents[0];
                Out.buffer = "";
                for (int i = 0; i < contents.length; i++) {
                    log(contents[i], color, logLevel);
                }

            }
            if (logHistory.size() > PropertyLoader.getMaxHistory()) {
                logHistory.remove(0);
            }
        }
    }


    private static void writeToFile(String content) {
        File file = new File(fileOutPut);
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Failed to create file: " + fileOutPut);
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String caller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement i : stackTrace) {
            String a = i.getClassName();
            if (!(a.startsWith("io.github.et.conopt4j") || a.startsWith("java"))) {
                return a;
            }
        }
        return "";
    }


    @Override
    public String toString() {
        return level.toString() + "\t" + fileOutPut;
    }

    private static ArrayList<String> logHistory = new ArrayList<String>();

    public static ArrayList<String> getLogHistory() {
        return logHistory;
    }

    public static void setFileOutPut(String fileOutPut) {
        Logger.fileOutPut = fileOutPut;
    }

}
