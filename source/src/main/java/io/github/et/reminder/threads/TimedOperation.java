package io.github.et.reminder.threads;

import javafx.application.Platform;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Timer;

import static io.github.et.reminder.threads.ServerStream.pw;
import static io.github.et.reminder.util.JsonReader.getTime;
import static io.github.et.reminder.util.TTS.playTextToSpeech;
import static io.github.et.reminder.util.XlsxReaderKt.getContent;
import static javafx.application.Application.launch;


public class TimedOperation implements Runnable{

    @Override
    public void run() {
        Timer timer = new Timer();
        for (; ; ) {
            LocalDateTime now = LocalDateTime.now();
            if (getTime().contains(now.getHour()+":"+now.getMinute())) {
                pw.println("exit");

                StringBuilder cnt = new StringBuilder();
                for (String i : getContent()) {
                    cnt.append(i + "；");
                }
                try {
                    playTextToSpeech(cnt.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}