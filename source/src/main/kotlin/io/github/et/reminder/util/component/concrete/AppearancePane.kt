package io.github.et.reminder.util.component.concrete

import io.github.et.reminder.MainApplication
import io.github.et.reminder.MainApplication.imgView
import io.github.et.reminder.MainApplication.imgView1
import io.github.et.reminder.component.SettingsPaneInner
import io.github.et.reminder.component.WrongCfgException
import io.github.et.reminder.util.BackgroundImage
import io.github.et.reminder.util.JsonReader
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream

class AppearancePane(val stage: Stage):SettingsPaneInner() {
    private val fileChooser= FileChooser()
    var file: File? =null
    private val text1=Text()
    private val text2=Text()
    val textF=TextField()
    private val button= Button()
    private var text3 = Text()
    private var onTop = ChoiceBox(FXCollections.observableArrayList("开","关"))

    @Throws(WrongCfgException::class)
    override fun updateCfg() {
        if(!File(textF.text).exists()&&textF.text!=""){
            throw WrongCfgException("外观->背景图片：文件不存在")
        }else {
            JsonReader.bgImage = textF.text
        }
        Platform.runLater {
            try {
                val bgImagePath = BackgroundImage.getPath()

                val img = Image(bgImagePath)
                imgView.setImage(img)
                imgView.setOpacity(0.2)
                val ratio = img.width / img.height
                imgView.setPreserveRatio(true)
                imgView.setFitHeight(stage.getHeight())
                imgView.setFitWidth(stage.getHeight() * ratio)
                imgView.setMouseTransparent(true)
                setLeftAnchor(imgView, stage.getWidth() / 2 - imgView.getFitWidth() / 2 - 30)
                imgView1.setImage(img)
                imgView1.setOpacity(0.2)
                imgView1.setPreserveRatio(true)
                imgView1.setFitHeight(stage.getHeight())
                setLeftAnchor(imgView1, stage.getWidth() / 2 - imgView.getFitWidth() / 2 - 30)
                imgView1.setFitWidth(stage.getHeight() * ratio)
                imgView1.setMouseTransparent(true)

            } catch (ignored: java.lang.Exception) {
            }
        }
        if(onTop.value=="开") {
            JsonReader.alwaysOnTop=true
            Platform.runLater{stage.isAlwaysOnTop=true}
        }else{
            JsonReader.alwaysOnTop=false
            Platform.runLater{stage.isAlwaysOnTop=false}
        }
    }
    init{
        if(JsonReader.bgImage==""){
            file=null
        }else{
            file=File(JsonReader.bgImage)
        }
        val font = Font.loadFont(FileInputStream("./zh-cn.ttf"), 30.0)
        text1.text="内容"
        text1.font= Font.font(font.family, FontWeight.NORMAL, 30.0)
        setTopAnchor(text1,0.0)
        setLeftAnchor(text1,30.0)
        text2.text="背景图片"
        text2.font = Font.font(font.family, FontWeight.NORMAL, 20.0)
        setTopAnchor(text2,50.0)
        setLeftAnchor(text2,30.0)
        textF.text=JsonReader.bgImage
        setTopAnchor(textF,100.0)
        setLeftAnchor(textF,30.0)
        textF.font=Font.font(font.family, FontWeight.NORMAL, 15.0)
        textF.minWidth=500.0
        textF.maxWidth=500.0
        button.style = "-fx-border-style:solid;" +
                "-fx-background-color:white;" + "-fx-border-radius:20"
        button.text="  选  择  "
        button.font=Font.font(font.family, FontWeight.NORMAL, 15.0)
        setTopAnchor(button,100.0)
        setLeftAnchor(button,550.0)
        button.setOnAction{
            Platform.runLater{file=selectFile(stage)}
        }
        text3.text="窗口总是居于最上层"
        text3.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setTopAnchor(text3,150.0)
        setLeftAnchor(text3,30.0)
        if(JsonReader.alwaysOnTop){
            onTop.value="开"
        }else{
            onTop.value="关"
        }
        setTopAnchor(onTop,150.0)
        setLeftAnchor(onTop,550.0)
        this.children.add(text1)
        this.children.add(text2)
        this.children.add(textF)
        this.children.add(button)
        this.children.add(onTop)
        this.children.add(text3)
    }

    fun selectFile(stage: Stage): File? {
        try {
            fileChooser.title="选择文件"
            fileChooser.selectedExtensionFilter= FileChooser.ExtensionFilter("Excel表格文件", "*.xlsx")
            fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter("图片文件", "*.gif","*.png","*.jpg","*.jpeg")
            )
            var file1= fileChooser.showOpenDialog(stage)
            Platform.runLater {
                if (file1 != null) {
                    textF.text = file1.path
                }
            }
            return file1
        }catch (e:Exception){
            return file
        }

    }

}