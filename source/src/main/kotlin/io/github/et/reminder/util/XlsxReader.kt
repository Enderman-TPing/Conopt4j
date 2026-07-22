package io.github.et.reminder.util

import io.github.et.reminder.util.JsonReader.after
import io.github.et.reminder.util.JsonReader.before
import io.github.et.reminder.util.JsonReader.spliter
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class XlsxReader
fun getContent():ArrayList<String> {
    val formatter = DateTimeFormatter.ofPattern("HH")
    val stream = FileInputStream("值日生安排.xlsx")
    val workbook: Workbook = XSSFWorkbook(stream)
    val sheet = workbook.getSheetAt(0)
    val now = LocalDateTime.now()
    val currentWeekday = now.dayOfWeek.toString()
    val content = ArrayList<String>()
    val currentTime = now.format(formatter)
    val splitHour= spliter.split(":")[0].toInt()
    val splitMinute= spliter.split(":")[1].toInt()
    var wkd by Delegates.notNull<Int>()
    when (currentWeekday) {
        "MONDAY" -> {
            wkd = 1
        }

        "TUESDAY" -> {
            wkd = 2
        }

        "WEDNESDAY" -> {
            wkd = 3
        }

        "THURSDAY" -> {
            wkd = 4
        }

        "FRIDAY" -> {
            wkd = 5
        }

        "SATURDAY" -> {
            wkd = 6
        }

        "SUNDAY" -> {
            wkd = 7
        }

    }
    var flag = 1
    if (before!="") {
        content.add(before)
    }
    try {
        while (sheet.getRow(flag).getCell(0) != null) {
            if (sheet.getRow(flag).getCell(0).toString() == " " || sheet.getRow(flag).getCell(0)
                    .toString() == "" || sheet.getRow(flag).getCell(0).toString() == "null"
            ) {
                break
            } else {
                if (sheet.getRow(flag).getCell(wkd) == null) {
                    flag++
                    continue
                } else if (sheet.getRow(flag).getCell(wkd).toString() == "null" || sheet.getRow(flag).getCell(wkd)
                        .toString() == "" || sheet.getRow(flag).getCell(wkd).toString() == " "
                ) {
                    flag++
                    continue
                } else if(sheet.getRow(flag).getCell(0).toString().endsWith("(上午)")||sheet.getRow(flag).getCell(0).toString().endsWith("（上午）")||sheet.getRow(flag).getCell(0).toString().endsWith("（上午)")||sheet.getRow(flag).getCell(0).toString().endsWith("(上午）")){
                    if(now.hour>splitHour||(now.hour==splitHour&&now.minute>=splitMinute)){
                        flag++
                        continue
                    }else{
                        content.add("${sheet.getRow(flag).getCell(0)}：\t${sheet.getRow(flag).getCell(wkd)}".replace("(上午)","").replace("（上午)","").replace("(上午）","").replace("（上午）",""))
                        flag++
                    }
                } else if(sheet.getRow(flag).getCell(0).toString().endsWith("(下午)")||sheet.getRow(flag).getCell(0).toString().endsWith("（下午）")||sheet.getRow(flag).getCell(0).toString().endsWith("（下午)")||sheet.getRow(flag).getCell(0).toString().endsWith("(下午）")){
                    if(now.hour<splitHour||(now.hour==splitHour&&now.minute<splitMinute)){
                        flag++
                        continue
                    }else{
                        content.add("${sheet.getRow(flag).getCell(0)}：\t${sheet.getRow(flag).getCell(wkd)}".replace("(下午)","").replace("（下午)","").replace("(下午）","").replace("（下午）",""))
                        flag++
                    }
                }else {
                    content.add("${sheet.getRow(flag).getCell(0)}：\t${sheet.getRow(flag).getCell(wkd)}")
                    flag++
                }
            }
        }
    }catch (_:NullPointerException){}

    if(content.isEmpty()){
        content.add("今日无值日生安排")
        return content
    }else if(content.size==1&&content[0]== before){
        content.clear()
        content.add("今日无值日生安排")
        return content
    }
    if(after!=""){
        content.add(after)
    }
    //TODO Read before, after
    return content
}
