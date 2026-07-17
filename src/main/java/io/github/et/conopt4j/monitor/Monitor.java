package io.github.et.conopt4j.monitor;


import java.lang.management.ManagementFactory;


public class Monitor {
    public static String getMonitor() {
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
        return "System Arch: " + sArch + " | Name: " + sName + " | Version: " + sVersion + " | Available Processors: " + availableProcessors + "\n"+
                "Thread Count: " + tc + " | Daemon Count: " + daemon + " | System Load: " + systemLoad + "\n"+
                "Total Memory: " + total + " | Max: " + max + " | Free:" + free + " | used " + used;

    }
}