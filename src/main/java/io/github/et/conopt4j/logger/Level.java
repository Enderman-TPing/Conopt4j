package io.github.et.conopt4j.logger;

/**
 * The levels contain Level.INFO, Level.DEBUG and Level.FINE <br/>
 * For Level.INFO, you can call: info(), error(), fatal() <br/>
 * For Level.DEBUG, you can call: info(), error(), fatal(), debug(), severe(), fine() <br/>
 * For Level.FINE, you can call: info(), error(), fatal(), severe(), fine() <br/>
 * If you call the functions that does not match the level you have declared, a LevelNotMatchException will be thrown
 * @author Enderman-Teleporting
 */
public enum Level {
    INFO,DEBUG,FINE
}