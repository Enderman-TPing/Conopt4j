package io.github.et.conopt4j.logger


/**
 * The levels contain Level.INFO, Level.DEBUG and Level.FINE
 *
 * For Level.INFO, you can call: info(), error(), fatal()
 *
 * For Level.DEBUG, you can call: info(), error(), fatal(), debug(), severe(), fine()
 *
 * For Level.FINE, you can call: info(), error(), fatal(), severe(), fine()
 *
 * If you call the functions that does not match the level you have declared, a LevelNotMatchException will be thrown
 *
 * @author Enderman-Teleporting
 */
enum class Level(val level:String) {
    INFO("Level.INFO"), DEBUG("Level.DEBUG"), FINE("Level.FINE")

}