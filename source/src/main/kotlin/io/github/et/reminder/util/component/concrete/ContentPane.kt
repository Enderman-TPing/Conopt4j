package io.github.et.reminder.util.component.concrete

import io.github.et.reminder.component.SettingsPaneInner
import io.github.et.reminder.component.WrongCfgException
import io.github.et.reminder.util.JsonReader
import io.github.et.reminder.util.JsonReader.before
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ContentPane(stage: Stage): SettingsPaneInner() {
    private val fileChooser=FileChooser()
    var file:File? =null
    private val text1:Text=Text()
    private val text2:Text= Text()
    private val button1:Button= Button("  导  入  ")
    private val button2:Button= Button("  编  辑  ")
    private val text3:Text= Text()
    val textF1:TextArea=TextArea()
    private val text4:Text=Text()
    val textF2:TextArea=TextArea()
    private val text5=Text()
    val textF3=TextField()
    private val text6=Text()
    val textF4=TextField()
    @Throws(WrongCfgException::class)
    override fun updateCfg() {
        if(file!=null &&file?.isFile == true){
            val input=FileInputStream(file)
            val output=FileOutputStream("./值日生安排.xlsx")
            val bts=input.readAllBytes()
            output.write(bts)
            output.flush()
            output.close()
            input.close()
            file=null
        }else if(file!=null){
            throw WrongCfgException("内容->编辑提醒内容或导入：选择的文件不存在")
        }else{}
        before =textF1.text
        JsonReader.after=textF2.text
        try{
            if(textF3.text.toInt() in 0..23 &&textF3.text.toInt() in 0 until 60){
                JsonReader.spliter=textF3.text+":"+textF4.text
            }else{
                throw Exception()
            }
        }catch (e:Exception){
            throw WrongCfgException("内容->上下午分界时间：输入有误")
        }
    }
    init{
        val font = Font.loadFont(FileInputStream("./zh-cn.ttf"), 30.0)
        text1.text="内容"
        text1.font=Font.font(font.family, FontWeight.NORMAL, 30.0)
        setTopAnchor(text1,0.0)
        setLeftAnchor(text1,30.0)
        text2.text="编辑提醒内容或导入"
        text2.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setTopAnchor(text2,50.0)
        setLeftAnchor(text2,30.0)
        button1.style = "-fx-border-style:solid;" +
                "-fx-background-color:white;" + "-fx-border-radius:20"
        button2.style = "-fx-border-style:solid;" +
                "-fx-background-color:white;" + "-fx-border-radius:20"
        button1.font=Font.font(font.family, FontWeight.NORMAL, 17.0)
        button2.font=Font.font(font.family, FontWeight.NORMAL, 17.0)
        AnchorPane.setLeftAnchor(button1,30.0)
        AnchorPane.setTopAnchor(button1,100.0)
        AnchorPane.setLeftAnchor(button2,300.0)
        AnchorPane.setTopAnchor(button2,100.0)
        button1.setOnAction{
            Platform.runLater{file=selectFile(stage)}
        }
        button2.setOnAction {
            stage.isAlwaysOnTop=false
            d.open(File("./值日生安排.xlsx"))
        }
        text3.text="前缀"
        text3.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setLeftAnchor(text3,30.0)
        setTopAnchor(text3,170.0)
        textF1.text=before
        textF1.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        textF1.maxWidth=500.0
        textF1.maxHeight=150.0
        setLeftAnchor(textF1,30.0)
        setTopAnchor(textF1,200.0)
        text4.text="后缀"
        text4.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setLeftAnchor(text4,30.0)
        setTopAnchor(text4,400.0)
        textF2.text=JsonReader.after
        textF2.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        textF2.maxWidth=500.0
        textF2.maxHeight=150.0
        setLeftAnchor(textF2,30.0)
        setTopAnchor(textF2,440.0)
        text5.text="上下午分界时间"
        text5.font=Font.font(font.family, FontWeight.NORMAL, 20.0)
        setLeftAnchor(text5,30.0)
        setTopAnchor(text5,630.0)
        textF3.text=JsonReader.spliter.split(":")[0]
        textF4.text = JsonReader.spliter.split(":")[1]
        textF3.font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
        textF4.font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
        text6.font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
        setTopAnchor(textF3,680.0)
        setLeftAnchor(textF3,30.0)
        setTopAnchor(textF4,680.0)
        setLeftAnchor(textF4,250.0)

        this.children.add(text1)
        this.children.add(text2)
        this.children.add(button1)
        this.children.add(button2)
        this.children.add(text3)
        this.children.add(textF1)
        this.children.add(text4)
        this.children.add(textF2)
        this.children.add(text5)
        this.children.add(text6)
        this.children.add(textF3)
        this.children.add(textF4)

    }

    fun selectFile(stage: Stage): File? {
        try {
            fileChooser.title = "选择文件"
            fileChooser.selectedExtensionFilter = ExtensionFilter("Excel表格文件", "*.xlsx")
            fileChooser.extensionFilters.addAll(
                ExtensionFilter("Excel表格文件", "*.xlsx")
            )
            return fileChooser.showOpenDialog(stage)
        }catch (e:Exception){
            return null
        }
    }

    companion object {
        private val d: Desktop = Desktop.getDesktop()
    }
}