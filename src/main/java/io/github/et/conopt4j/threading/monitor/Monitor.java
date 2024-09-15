package io.github.et.conopt4j.threading.monitor;

import io.github.et.conopt4j.launcher.PropertyLoader;
import io.github.et.conopt4j.streams.LineProcessor;
import io.github.et.conopt4j.streams.MOut;

import java.lang.management.ManagementFactory;

public class Monitor implements Runnable{
    private static final long interval= PropertyLoader.getInterval();

    @Override
    public void run() {
        for(;;){
            Runtime runtime=Runtime.getRuntime();
            String sName=ManagementFactory.getOperatingSystemMXBean().getName();
            int tc=ManagementFactory.getThreadMXBean().getThreadCount();
            int daemon=ManagementFactory.getThreadMXBean().getDaemonThreadCount();
            double systemLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
            int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            String sArch=ManagementFactory.getOperatingSystemMXBean().getArch();
            String sVersion=ManagementFactory.getOperatingSystemMXBean().getVersion();
            long total=runtime.totalMemory();
            long max=runtime.maxMemory();
            long free=runtime.freeMemory();
            long used=max-free;
            String result=
                    "System Arch:\t"+sArch+"\tName:"+sName+"\tVersion:\t"+sVersion+"\tAvailable Processors:\t"+availableProcessors+"\n"+
                    "Thread Count:\t"+tc+"\tDaemon Count:\t"+daemon+"\tSystem Load:\t"+systemLoad+"\n"+
                    "Total Memory:\t"+total+"\tMax:\t"+max+"\tFree:\t"+free+"\tused\t"+used;
            LineProcessor.setMonitor(result);
            MOut.update0();
            MOut.update();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
