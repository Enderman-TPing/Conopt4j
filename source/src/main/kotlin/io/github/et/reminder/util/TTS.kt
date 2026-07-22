package io.github.et.reminder.util

import java.io.*
import java.net.*


object TTS {

    @JvmStatic
    @Throws(IOException::class)
    fun playTextToSpeech(content: String) {
        if(content=="今日无值日生安排；"){
            return
        }
        try {
            val command =
                "powershell.exe Add-Type -AssemblyName System.Speech; \$tts = New-Object System.Speech.Synthesis.SpeechSynthesizer; \$tts.Speak('${content.replace("\n", "。")}')"
            val powerShellProcess = Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
