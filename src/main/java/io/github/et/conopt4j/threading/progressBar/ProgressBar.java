package io.github.et.conopt4j.threading.progressBar;

import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.conopt4j.launcher.PropertyLoader;
import io.github.et.conopt4j.threading.monitor.Monitor;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.Collections;

public class ProgressBar {
    private static ArrayList<ProgressBar> progressBars = new ArrayList<ProgressBar>();
    private String prefix="";
    private String suffix="";
    private int progress=0;
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public void setProgress(int progress) {
        if(progress<0||progress>100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
    }
    public void show(){
        if(!progressBars.contains(this)){
            progressBars.add(this);
        }
        update();
    }
    public static void update(){
        synchronized(Launcher.getStatusList()){
            Launcher.getStatusList().clear();
            if(PropertyLoader.useMonitor()) {
                Monitor.getMonitor();
            }
            AttributedStringBuilder sb=new AttributedStringBuilder();
            for(ProgressBar progressBar:progressBars){
                sb.append(progressBar.prefix);
                int a=progressBar.progress/2;
                sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
                sb.append("-".repeat(a));
                sb.style(AttributedStyle.DEFAULT);
                sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
                sb.append("-".repeat(50 - a));
                sb.style(AttributedStyle.DEFAULT);
                sb.append(progressBar.suffix);
                Launcher.getStatusList().add(sb.toAttributedString());
            }
            Launcher.status.update(Collections.emptyList());
            Launcher.status.update(Launcher.getStatusList());
        }

    }

    public void hide(){
        progressBars.remove(this);
        update();
    }
}
