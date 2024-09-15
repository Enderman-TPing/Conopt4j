package io.github.et.conopt4j.logger

import io.github.et.conopt4j.exceptions.LevelNotMatchException
import io.github.et.conopt4j.exceptions.RepeatedLoggerDeclarationException
import io.github.et.conopt4j.launcher.Launcher.isAnsiInstalled
import io.github.et.conopt4j.launcher.PropertyLoader
import io.github.et.conopt4j.streams.Err
import io.github.et.conopt4j.streams.LineProcessor
import io.github.et.conopt4j.streams.Out
import io.github.et.conopt4j.style.Color
import org.fusesource.jansi.AnsiConsole
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * The class Logger is a class for logging as it literally shows. Only one Logger can be used in a certain project. If the Logger is used in different classes, you should call <h4>Logger.getDeclaredLogger</h4> to get the declared logger
 *
 * For more information, plz see the JavaDoc on the functions
 * @author Enderman-Teleporting
 */
@Suppress("unused")
class Logger {

    init {
        if (declared) {
            throw RepeatedLoggerDeclarationException("One Logger has already been declared")
        }
        if(!isAnsiInstalled){
            AnsiConsole.systemInstall()
            isAnsiInstalled = true
        }
        log=this
        declared = true
    }


    /**
     * The [ INFO ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    fun info(content: String, vararg f: Any?) {
        log(content, info, "INFO", f)
    }

    /**
     * The [ WARN ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    fun warn(content: String, vararg f: Any?) {
        log(content, warn, "WARN", f)
    }

    /**
     * The [ ERROR ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    fun error(content: String, vararg f: Any?) {
        log(content, error, "ERROR", f)
    }

    /**
     * The [ FATAL ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     */
    fun fatal(content: String, vararg f: Any?) {
        log(content, fatal, "FATAL", f)
    }

    /**
     * The [ DEBUG ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Level.DEBUG
     */
    fun debug(content: String, vararg f: Any?) {
        if (level != Level.DEBUG) {
            throw LevelNotMatchException("As Level.DEBUG is needed as logger level, current level $level")
        }
        log(content, debug, "DEBUG", f)
    }

    /**
     * The [ FINE ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Level.DEBUG or Level.FINE
     */
    fun fine(content: String, vararg f: Any?) {
        if (level != Level.DEBUG && level != Level.FINE) {
            throw LevelNotMatchException("As Level.FINE is needed as logger level, current level $level")
        }
        log(content, fine, "FINE", f)
    }

    /**
     * The [ SEVERE ] log output
     *
     * @author Enderman-Teleporting
     * @param content (String) -> Log content
     * @param f (Object...) -> Log format
     * @throws LevelNotMatchException -> Called when your level is not Level.DEBUG or Level.FINE
     */
    fun severe(content: String, vararg f: Any?) {
        if (level != Level.DEBUG && level != Level.FINE) {
            throw LevelNotMatchException("As Level.FINE is needed as logger level, current level $level")
        }
        log(content, severe, "SEVERE", f)
    }

    /**
     * The blank [ INFO ] log output
     *
     * @author Enderman-Teleporting
     */
    fun info(){ info("") }
    /**
     * The blank [ WARN ] log output
     *
     * @author Enderman-Teleporting
     */
    fun warn(){ warn("") }
    /**
     * The blank [ ERROR ] log output
     *
     * @author Enderman-Teleporting
     */
    fun error(){ error("")}
    /**
     * The blank [ SEVERE ] log output
     *
     * @author Enderman-Teleporting
     */
    fun severe(){ severe("") }
    /**
     * The blank [ FINE ] log output
     *
     * @author Enderman-Teleporting
     */
    fun fine(){ fine("") }
    /**
     * The blank [ DEBUG] log output
     *
     * @author Enderman-Teleporting
     */
    fun debug(){ debug("") }
    /**
     * The blank [ FATAL ] log output
     *
     * @author Enderman-Teleporting
     */
    fun fatal(){ fatal("") }

    private fun log(content: String, color: Color, logLevel: String, vararg f: Any) {
        val contents = if (content.contains("\n")) content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() else arrayOf(content)
        val date = Date()
        val sb = StringBuilder()

        for (cnt in contents) {
            val formattedContent = String.format(cnt, *f)
            val output = String.format(format, color, logLevel, fmt.format(date), caller(),formattedContent,"\n")
            sb.append(output)
            Out.OUT.printf(output)
        }

        if (fileOutPut != null) {
            writeToFile(sb.toString())
        }
        LineProcessor.setCurrentLine("")
    }
    private fun logErr(content: String, color: Color, logLevel: String, vararg f: Any){
        val contents = if (content.contains("\n")) content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() else arrayOf(content)
        val date = Date()
        val sb = StringBuilder()

        for (cnt in contents) {
            val formattedContent = String.format(cnt, *f)
            val output = String.format(format, color.toString(), logLevel, fmt.format(date), caller(),formattedContent,"\n")
            sb.append(output)
            Err.ERR.printf(output)
        }

        if (fileOutPut != null) {
            writeToFile(sb.toString())
        }
        LineProcessor.setCurrentLine("")
    }

    private fun writeToFile(content: String) {
        val file = File(fileOutPut!!)
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw IOException("Failed to create file: " + fileOutPut)
            }
            FileOutputStream(file, true).use { fos ->
                fos.write(
                    content.replace("\u001b[31m", "")
                        .replace("\u001b[32m", "")
                        .replace("\u001b[33m", "")
                        .replace("\u001b[34m", "")
                        .replace("\u001b[35m", "")
                        .replace("\u001b[36m", "")
                        .replace("\u001b[37m", "")
                        .replace("\u001b[0m", "")
                        .toByteArray()
                )
                fos.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun caller(): String {
        val stackTrace = Thread.currentThread().stackTrace
        for (i in stackTrace) {
            val a = i.className
            if (!(a.startsWith("io.github.et.conopt4j") || a.startsWith("java"))) {
                return a
            }
        }
        return stackTrace[4].className
    }




    override fun toString(): String {
        return level.toString() + "\t" + fileOutPut
    }

    companion object {
        private var log: Logger? = null
        private var level = PropertyLoader.getLevel()
        private val format = PropertyLoader.getFormat().toString()
        private var fileOutPut: String? = PropertyLoader.getLogOutPut()
        private var declared = false
        private val fmt = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")

        private var info = PropertyLoader.getInfo()
        private var warn = PropertyLoader.getWarn()
        private var error = PropertyLoader.getError()
        private var fatal = PropertyLoader.getFatal()
        private var debug = PropertyLoader.getDebug()
        private var fine = PropertyLoader.getFine()
        private var severe = PropertyLoader.getSevere()

        @JvmStatic
        val logger: Logger
            /**
             * A function to get the Logger that you have already declared
             * @author Enderman-Teleporting
             * @return The Logger that you have declared in other classes
             * @throws LoggerNotDeclaredException -> Called when no Logger has been declared
             */
            get() {
                return if (log != null) log!! else (Logger().also { log = it })
            }
    }
}
