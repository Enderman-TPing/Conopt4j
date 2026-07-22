package io.github.et.conopt4j.launcher;

import io.github.et.conopt4j.logger.Level;
import io.github.et.conopt4j.streams.Err;
import io.github.et.conopt4j.style.Color;

import java.io.InputStream;
import java.util.Properties;

/**
 * The structure of the configuration file should be like the example below:
 *
 * <pre>
 * {@code
 * conopt4j.logger.level = Level.DEBUG
 * conopt4j.logger.info = Color.WHITE
 * conopt4j.logger.warn = Color.YELLOW
 * conopt4j.logger.debug = Color.CYAN
 * conopt4j.logger.error = Color.RED
 * conopt4j.logger.fatal = Color.PURPLE
 * conopt4j.logger.severe = Color.RED
 * conopt4j.logger.fine = Color.BLUE
 * conopt4j.logger.useTrace = true
 * conopt4j.logger.useDate = true
 * conopt4j.logger.maxHistory = 1024
 * conopt4j.command.prompt = >
 * conopt4j.monitor.use = true
 * conopt4j.status.interval = 500
 * conopt4j.logger.output = a.log
 * }
 * </pre>
 *
 * The not provided ones will be regarded as default.
 *
 * @author Enderman-TPing
 */
public class PropertyLoader {
    private static Level level=Level.INFO;
    private static Color info=Color.WHITE;
    private static Color warn=Color.WHITE;
    private static Color debug=Color.WHITE;
    private static Color error=Color.WHITE;
    private static Color fatal=Color.WHITE;
    private static Color severe=Color.WHITE;
    private static Color fine=Color.WHITE;
    private static boolean useDate=true;
    private static boolean useTrace=true;
    private static int maxHistory=1024;
    private static String prompt = ">";
    private static boolean useMonitor = true;
    private static String logOutPut=null;
    private static long interval=500;
    private static final Properties properties = new Properties();
    

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

    public static int getMaxHistory() {return maxHistory;}

    public static String getPrompt() {return prompt;}

    public static boolean isUseDate() {return useDate;}

    public static boolean isUseTrace() {return useTrace;}

    public static boolean useMonitor() {return useMonitor;}

    public static void loadProperties(InputStream in){
        try{
            properties.load(in);
        }catch (Exception e){
            Err.ERR.println("Load config file error! Using default values");
            return;
        }
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
            maxHistory = Integer.parseInt(properties.getProperty("conopt4j.logger.maxHistory"));
            prompt = properties.getProperty("conopt4j.command.prompt");
            useDate=Boolean.parseBoolean(properties.getProperty("conopt4j.logger.useDate"));
            useTrace=Boolean.parseBoolean(properties.getProperty("conopt4j.logger.useTrace"));
            useMonitor=Boolean.parseBoolean(properties.getProperty("conopt4j.monitor.use"));
            interval=Long.parseLong(properties.getProperty("conopt4j.status.interval"));
        }catch (Exception ignored){}
        if(maxHistory<=64){
            throw new IllegalArgumentException("comopt4j.logger.maxHistory must be at least 64");
        }

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
