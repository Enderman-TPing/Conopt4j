package io.github.et.conopt4j.threading.status;


import io.github.et.conopt4j.launcher.Launcher;
import org.jline.utils.AttributedString;

import java.lang.management.ManagementFactory;


public class Monitor {
    public static void getMonitor() {
        Runtime runtime = Runtime.getRuntime();
        String sName = ManagementFactory.getOperatingSystemMXBean().getName();
        int tc = ManagementFactory.getThreadMXBean().getThreadCount();
        int daemon = ManagementFactory.getThreadMXBean().getDaemonThreadCount();
        double systemLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        String sArch = ManagementFactory.getOperatingSystemMXBean().getArch();
        String sVersion = ManagementFactory.getOperatingSystemMXBean().getVersion();
        long total = runtime.totalMemory();
        long max = runtime.maxMemory();
        long free = runtime.freeMemory();
        long used = max - free;
        Launcher.getStatusList().add(new AttributedString(Space.getSpace() + "System Arch: " + sArch + " | Name: " + sName + " | Version: " + sVersion + " | Available Processors: " + availableProcessors));
        Launcher.getStatusList().add(new AttributedString(Space.getSpace() + "Thread Count: " + tc + " | Daemon Count: " + daemon + " | System Load: " + systemLoad));
        Launcher.getStatusList().add(new AttributedString(Space.getSpace() + "Total Memory: " + total + " | Max: " + max + " | Free:" + free + " | used " + used));
    }
}