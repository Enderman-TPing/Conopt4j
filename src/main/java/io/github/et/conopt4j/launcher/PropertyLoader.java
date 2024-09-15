package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.logger.Level;
import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.style.Color;
import io.github.et.conopt4j.style.Style;
import io.github.et.conopt4j.style.Style_headless;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * The structure of the configuration file should be like the example below:
 * <p>
 * <pre>
 * {@code
 * conopt4j.logger.format = Style.HINT
 * conopt4j.logger.level = Level.DEBUG
 * conopt4j.logger.info = Color.WHITE
 * conopt4j.logger.warn = Color.YELLOW
 * conopt4j.logger.debug = Color.CYAN
 * conopt4j.logger.error = Color.RED
 * conopt4j.logger.fatal = Color.PURPLE
 * conopt4j.logger.severe = Color.RED
 * conopt4j.logger.fine = Color.BLUE
 * conopt4j.logger.output = a.log
 * conopt4j.monitor.interval = 500
 * }
 * </pre>
 * </p>
 *
 * The not provided ones will be regarded as default.
 *
 * @author Enderman-TPing
 */
public class PropertyLoader {
    private static Style format=Style.HINT;
    private static Style_headless format_headless=Style_headless.HINT;
    private static Level level=Level.INFO;
    private static Color info=Color.WHITE;
    private static Color warn=Color.WHITE;
    private static Color debug=Color.WHITE;
    private static Color error=Color.WHITE;
    private static Color fatal=Color.WHITE;
    private static Color severe=Color.WHITE;
    private static Color fine=Color.WHITE;
    private static String logOutPut=null;
    private static final Properties properties = new Properties();
    private static long interval=500;

    public static Style getFormat() {
        return format;
    }


    public static Style_headless getFormat_headless() {
        return format_headless;
    }

    public static Level getLevel() {
        return level;
    }

    public static Color getInfo() {
        return info;
    }

    public static Color getWarn() {
        return warn;
    }

    public static Color getDebug() {
        return debug;
    }

    public static Color getError() {
        return error;
    }

    public static Color getFatal() {
        return fatal;
    }

    public static Color getSevere() {
        return severe;
    }

    public static Color getFine() {
        return fine;
    }

    public static String getLogOutPut() {
        return logOutPut;
    }

    public static void loadProperties(InputStream in){
        try{
            properties.load(in);
        }catch (Exception e){
            Err.ERR.println("Load config file error! Using default values");
            return;
        }
        format=properties.getProperty("conopt4j.logger.format","Style.HINT").equals("Style.ALL")?Style.ALL:Style.HINT;
        format_headless=properties.getProperty("conopt4j.logger.format","Style.HINT").equals("Style.ALL")?Style_headless.ALL:Style_headless.HINT;
        String tmp=properties.getProperty("conopt4j.logger.level","Level.INFO");
        if(tmp.equals("Level.DEBUG")){
            level=Level.DEBUG;
        } else if (tmp.equals("Levels.FINE")) {
            level=Level.FINE;
        } else{
            level=Level.INFO;
        }
        info=getColor("info");
        warn=getColor("warn");
        error=getColor("error");
        fatal = getColor("fatal");
        debug = getColor("debug");
        severe = getColor("severe");
        fine = getColor("fine");
        tmp=properties.getProperty("conopt4j.logger.output","");
        logOutPut= tmp.isEmpty() ?null:tmp;
        try {
            interval = Long.parseLong(properties.getProperty("conopt4j.monitor.interval", "500"));
        }catch (Exception ignored){}

    }
    private static Color getColor(String level){
        String tmp=properties.getProperty("conopt4j.logger."+level,"Color.WHITE");
        return switch (tmp) {
            case "Color.BLUE" -> Color.BLUE;
            case "Color.YELLOW" -> Color.YELLOW;
            case "Color.CYAN" -> Color.CYAN;
            case "Color.GREEN" -> Color.GREEN;
            case "Color.PURPLE" -> Color.PURPLE;
            case "Color.RED" -> Color.RED;
            default -> Color.WHITE;
        };
    }

    public static long getInterval() {
        return interval;
    }
}
