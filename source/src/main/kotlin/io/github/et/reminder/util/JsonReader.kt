package io.github.et.reminder.util

import com.alibaba.fastjson2.JSONObject
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object JsonReader {
    init {
        val jsonObject = JSONObject()
        jsonObject["time"] = "12:10; 12:15"
        jsonObject["before"] = URLEncoder.encode("请以下同学做值日：", StandardCharsets.UTF_8)
        jsonObject["after"] = ""
        jsonObject["bgImage"]=""
        jsonObject["spliter"]="12:30"
        jsonObject["alwaysOnTop"]=true
        jsonObject["isMovable"]=false
        val jsonString = jsonObject.toJSONString()
        val jsonFile = File("./cfg.json")
        if (!jsonFile.exists()) {
            jsonFile.createNewFile()
            val a = BufferedWriter(FileWriter("./cfg.json", false))
            a.write(jsonString)
            a.close()
        }
    }

    @JvmStatic
    private val jsonReadFile = BufferedReader(FileReader("./cfg.json"))

    @JvmStatic
    val json = JSONObject.parseObject(jsonReadFile.readText())

    @JvmStatic
    var time: ArrayList<String> = ArrayList(if(json["time"]==null) emptyList() else json["time"].toString().split("; "))

    @JvmStatic
    var before: String = if(json["before"]==null) "" else URLDecoder.decode(json["before"].toString(),StandardCharsets.UTF_8)

    @JvmStatic
    var after: String = if(json["after"]==null) "" else URLDecoder.decode(json["after"].toString(),StandardCharsets.UTF_8)
    @JvmStatic
    var bgImage:String=if(json["bgImage"]==null) "" else URLDecoder.decode(json["bgImage"].toString(),StandardCharsets.UTF_8)

    @JvmStatic
    var spliter:String=json["spliter"].toString()

    @JvmStatic
    var alwaysOnTop:Boolean=json["alwaysOnTop"].toString().toBoolean()

    @JvmStatic
    var isMovable:Boolean=json["isMovable"].toString().toBoolean()

    @JvmStatic
    fun update() {
        val sb = StringBuilder()
        for (i in time.indices) {
            if (i == time.size - 1) {
                sb.append(time[i])
            } else {
                sb.append("${time[i]}; ")
            }
        }
        json["time"] = sb.toString()
        json["before"] = URLEncoder.encode(before,StandardCharsets.UTF_8)
        json["after"] = URLEncoder.encode(after,StandardCharsets.UTF_8)
        json["bgImage"] = URLEncoder.encode(bgImage,StandardCharsets.UTF_8)
        json["spliter"] = spliter
        json["alwaysOnTop"] = alwaysOnTop
        json["inMovable"]= isMovable
        val finalJson = json.toJSONString()
        val a = BufferedWriter(FileWriter("./cfg.json", false))
        a.write(finalJson)
        a.close()
    }

}