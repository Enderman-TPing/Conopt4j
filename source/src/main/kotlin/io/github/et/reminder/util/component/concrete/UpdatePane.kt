package io.github.et.reminder.util.component.concrete

import io.github.et.reminder.MainApplication.mover
import io.github.et.reminder.component.SettingsPaneInner
import io.github.et.reminder.util.JsonReader
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Hyperlink
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class UpdatePane(stage: Stage) : SettingsPaneInner() {
    val currentVersion:ArrayList<Int> = ArrayList()
    init{
        currentVersion.add(2)
        currentVersion.add(1)
        currentVersion.add(1)
    }
    private val text1 = Text()
    private val text2 = Text()
    private val checkForUpdate = Button("检查更新")
    private val text3 = Text()
    private var hyperlink: Hyperlink = Hyperlink("这个")
    private var text4 = Text()
    private var movable = ChoiceBox(FXCollections.observableArrayList("开","关"))

    override fun updateCfg() {
        if(movable.value=="开") {
            JsonReader.isMovable=true
            Platform.runLater{mover.isVisible=true}
        }else{
            JsonReader.isMovable=false
            Platform.runLater{mover.isVisible=false}
        }
    }
    init {
        val font = Font.loadFont(FileInputStream("./zh-cn.ttf"), 30.0)
        text1.text = "通用"
        text1.font = Font.font(font.family, FontWeight.NORMAL, 30.0)
        setTopAnchor(text1, 0.0)
        setLeftAnchor(text1, 30.0)
        text2.text = "版本号：2.1.0"
        text2.font = Font.font(font.family, FontWeight.THIN, 20.0)
        text2.fill = Color.GRAY
        setTopAnchor(text2, 50.0)
        setLeftAnchor(text2, 30.0)
        checkForUpdate.style = "-fx-border-style:solid;" +
                "-fx-background-color:white;" + "-fx-border-radius:20"
        setTopAnchor(checkForUpdate, 40.0)
        setLeftAnchor(checkForUpdate, 450.0)
        checkForUpdate.font = Font.font(font.family, FontWeight.NORMAL, 17.0)
        text3.text = "对使用方式有疑惑？请访问"
        text3.font = Font.font(font.family, FontWeight.NORMAL, 20.0)
        hyperlink.font = Font.font(font.family, FontWeight.NORMAL, 20.0)
        hyperlink.onAction = EventHandler { _: ActionEvent? ->
            try {
                stage.isAlwaysOnTop=false
                d.browse(URI("https://jpchs.github.io/Reminder/"))
            } catch (ignored: Exception) {
            }
        }
        checkForUpdate.onAction = EventHandler { _: ActionEvent? ->
            checkForUpdate.text = "检查更新中..."
            checkForUpdate.isDisable = true

            try {
                GlobalScope.launch {
                    val url = URL("https://jpchs.github.io/Reminder/latest")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0")

                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val version = reader.readLine()
                    val k=version.split(".")
                    Platform.runLater {
                        if (k[0].toInt() > currentVersion[0] ||
                            (k[0].toInt() == currentVersion[0] && k[1].toInt() > currentVersion[1]) ||
                            (k[0].toInt() == currentVersion[0] && k[1].toInt() == currentVersion[1] && k[2].toInt() > currentVersion[2])
                        ) {
                            checkForUpdate.text = "发现新版本: $version"
                            checkForUpdate.text = "下载更新中..."
                            val url = URL("https://github.com/JPCHS/Reminder/releases/download/v$version/Installer.exe")
                            val connection = url.openConnection()
                            connection.connect()

                            val input = BufferedInputStream(url.openStream())
                            val output = FileOutputStream(File.createTempFile("reminder-installer-temp","exe"))

                            val data = ByteArray(1024)
                            var count: Int
                            while (input.read(data, 0, 1024).also { count = it } != -1) {
                                output.write(data, 0, count)
                            }
                            output.flush()
                            output.close()
                            input.close()
                            var runtime=Runtime.getRuntime()
                            val command = "%temp%/reminder-installer-temp.exe"
                            var a = runtime.exec(command)
                            checkForUpdate.text="完成,请按提示安装"
                        } else {
                            checkForUpdate.text = "当前是最新版本"
                        }
                    }
                    reader.close()
                }
            } catch (e: Exception) {
                checkForUpdate.text = "检查更新失败"
                checkForUpdate.isDisable = false
            }
        }
        setTopAnchor(text3, 120.0)
        setLeftAnchor(text3, 30.0)
        setTopAnchor(hyperlink, 113.0)
        setLeftAnchor(hyperlink, 270.0)
        text4.text="窗口可移动"
        text4.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setTopAnchor(text4,170.0)
        setLeftAnchor(text4,30.0)
        if(JsonReader.isMovable){
            movable.value="开"
        }else{
            movable.value="关"
        }
        setTopAnchor(movable,170.0)
        setLeftAnchor(movable,450.0)
        children.add(text1)
        children.add(text2)
        children.add(checkForUpdate)
        children.add(text3)
        children.add(hyperlink)
        children.add(text4)
        children.add(movable)
    }

    companion object {
        private val d: Desktop = Desktop.getDesktop()
    }
}
