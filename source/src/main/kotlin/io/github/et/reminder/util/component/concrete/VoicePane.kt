package io.github.et.reminder.util.component.concrete

import io.github.et.reminder.component.SettingsPaneInner
import io.github.et.reminder.component.WrongCfgException
import io.github.et.reminder.util.JsonReader
import javafx.collections.FXCollections
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import java.io.FileInputStream
import kotlin.math.ceil

class VoicePane : SettingsPaneInner() {
    private val text1 = Text()
    private val text4 = Text()
    private val inner=InnerAnchor()
    private val timeScroll=ScrollPane(inner)
    @Throws(WrongCfgException::class)
    override fun updateCfg() {
        try{
            for (i in inner.textFields1.indices){
                if(inner.textFields1[i].text.toInt() in 0..23 &&inner.textFields2[i].text.toInt() in 0 until 60){
                    JsonReader.time=ArrayList()
                    JsonReader.time.add(inner.textFields1[i].text+":"+inner.textFields2[i].text)
                }else{
                    throw Exception()
                }
            }
        }catch(e:Exception){
            throw WrongCfgException("语音->播报时间：输入有误")
        }
    }


    init {
        val font = Font.loadFont(FileInputStream("./zh-cn.ttf"), 30.0)
        text1.text = "语音"
        text1.font = Font.font(font.family, FontWeight.NORMAL, 30.0)
        setTopAnchor(text1, 0.0)
        setLeftAnchor(text1, 30.0)
        text4.text="播报时间"
        text4.font = Font.font(font.family, FontWeight.NORMAL, 20.0)
        setTopAnchor(text4,50.0)
        setLeftAnchor(text4,30.0)
        timeScroll.vbarPolicy=ScrollPane.ScrollBarPolicy.AS_NEEDED
        timeScroll.hbarPolicy=ScrollPane.ScrollBarPolicy.AS_NEEDED
        timeScroll.setMinSize(500.0,100.0)
        timeScroll.setMaxSize(500.0,100.0)
        setTopAnchor(timeScroll,100.0)
        setLeftAnchor(timeScroll,30.0)
        children.add(text1)
        children.add(text4)
        children.add(timeScroll)
    }
}
private class InnerAnchor: AnchorPane() {
    val textFields1= ArrayList<TextField>()
    val textFields2 = ArrayList<TextField>()
    val hrT = ArrayList<Label>()
    val deleter=ArrayList<Label>()
    val plus=Label("                     +                     ")
    init{
        this.children.add(plus)
        for (i in 0..<JsonReader.time.size){
            textFields1.add(TextField(JsonReader.time[i].split(":")[0]))
            textFields2.add(TextField(JsonReader.time[i].split(":")[1]))
            hrT.add(Label("："))
            deleter.add(Label(" - "))
            setTopAnchor(textFields1[i],i*40.0)
            setLeftAnchor(textFields1[i],0.0)
            setTopAnchor(textFields2[i],i*40.0)
            setLeftAnchor(textFields2[i],220.0)
            setLeftAnchor(hrT[i],210.0)
            setTopAnchor(hrT[i],i*40.0)
            setLeftAnchor(deleter[i],440.0)
            setTopAnchor(deleter[i],i*40.0)
            deleter[i].style="-fx-border-style:dashed"
            textFields1[i].maxWidth=210.0
            textFields2[i].maxHeight=20.0
            textFields1[i].maxHeight=20.0
            textFields2[i].maxWidth=210.0
            textFields2[i].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
            textFields1[i].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
            hrT[i].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
            deleter[i].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 20.0)
            deleter[i].textFill=Color.BLUE
            this.children.add(textFields1[i])
            this.children.add(textFields2[i])
            this.children.add(hrT[i])
            this.children.add(deleter[i])
            deleter[i].setOnMouseClicked {
                try {
                    setTopAnchor(plus, getTopAnchor(plus) - 40)
                    this.children.remove(textFields1[i])
                    this.children.remove(textFields2[i])
                    this.children.remove(hrT[i])
                    this.children.remove(deleter.last())
                    textFields1.removeAt(i)
                    textFields2.removeAt(i)
                    hrT.removeAt(i)
                    deleter.removeLast()
                    for (j in i..<textFields1.size) {
                        setTopAnchor(textFields1[j], j * 40.0)
                        setLeftAnchor(textFields1[j], 0.0)
                        setTopAnchor(textFields2[j], j * 40.0)
                        setLeftAnchor(textFields2[j], 220.0)
                        setLeftAnchor(hrT[j], 210.0)
                        setTopAnchor(hrT[j], j * 40.0)
                    }
                } catch (_: Exception) {}


            }
            plus.font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 35.0)
            plus.textFill=Color.GREEN
            setLeftAnchor(plus,0.0)
            setTopAnchor(plus,deleter.size*40.0)
            plus.setOnMouseClicked {
                setTopAnchor(plus, getTopAnchor(plus)+40)
                textFields1.add(TextField())
                textFields2.add(TextField())
                hrT.add(Label("："))
                deleter.add(Label(" - "))
                setTopAnchor(textFields1[textFields1.size-1],(textFields1.size-1)*40.0)
                setLeftAnchor(textFields1[textFields1.size-1],0.0)
                setTopAnchor(textFields2[textFields2.size-1],(textFields2.size-1)*40.0)
                setLeftAnchor(textFields2[textFields2.size-1],220.0)
                setLeftAnchor(hrT[hrT.size-1],210.0)
                setTopAnchor(hrT[hrT.size-1],(textFields2.size-1)*40.0)
                setLeftAnchor(deleter[deleter.size-1],440.0)
                setTopAnchor(deleter[deleter.size-1],(textFields2.size-1)*40.0)
                deleter[deleter.size-1].style="-fx-border-style:dashed"
                textFields2[textFields2.size-1].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
                textFields1[textFields1.size-1].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
                hrT[hrT.size-1].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 15.0)
                deleter[deleter.size-1].font=Font.loadFont(FileInputStream("./zh-cn.ttf"), 20.0)
                deleter[deleter.size-1].textFill=Color.BLUE
                this.children.add(textFields1[textFields1.size-1])
                this.children.add(textFields2[textFields2.size-1])
                this.children.add(hrT[hrT.size-1])
                this.children.add(deleter[deleter.size-1])

                deleter[deleter.size-1].setOnMouseClicked {
                    try {
                        setTopAnchor(plus, getTopAnchor(plus) - 40)
                        this.children.remove(textFields1[textFields1.size - 1])
                        this.children.remove(textFields2[textFields2.size - 1])
                        this.children.remove(hrT[hrT.size - 1])
                        this.children.remove(deleter.last())
                        textFields1.removeAt(textFields1.size - 1)
                        textFields2.removeAt(textFields2.size - 1)
                        hrT.removeAt(hrT.size - 1)
                        deleter.removeLast()
                        for (j in textFields1.size - 1..<textFields1.size) {
                            setTopAnchor(textFields1[j], j * 40.0)
                            setLeftAnchor(textFields1[j], 0.0)
                            setTopAnchor(textFields2[j], j * 40.0)
                            setLeftAnchor(textFields2[j], 220.0)
                            setLeftAnchor(hrT[j], 210.0)
                            setTopAnchor(hrT[j], j * 40.0)

                        }
                    }catch (_: Exception){}
                }
            }
        }
    }
}