package io.github.et.conopt4j.threading.monitor;

import io.github.et.conopt4j.launcher.PropertyLoader;
import io.github.et.conopt4j.streams.LineProcessor;
import io.github.et.conopt4j.streams.MOut;
import org.jline.reader.LineReader;
import org.jline.terminal.Size;
import org.jline.utils.InfoCmp;

import java.lang.management.ManagementFactory;

import static io.github.et.conopt4j.launcher.Launcher.*;

public class Monitor implements Runnable{
    private static final long interval= PropertyLoader.getInterval();
    private static Size size;

    @Override
    public void run() {
        if (TERMINAL.getStringCapability(InfoCmp.Capability.change_scroll_region) == null||TERMINAL.getStringCapability(InfoCmp.Capability.clr_eol)==null) {
            return;
        }
        UseMonitor=true;
        for (;;) {
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
            String[] line= new String[]{
                    "System Arch: " + sArch + " | Name: " + sName + " | Version: " + sVersion + " | Available Processors: " + availableProcessors + "\n",
                    "Thread Count: " + tc + " | Daemon Count: " + daemon + " | System Load: " + systemLoad + "\n",
                    "Total Memory: " + total + " | Max: " + max + " | Free:" + free + " | used " + used
            };
            int maxLine=Math.max(Math.max(line[0].length(),line[1].length()),line[2].length());
            size=TERMINAL.getSize();
            int monitorStartRow= size.getRows()-4;
            if(size.getColumns()<maxLine||size.getRows()<5){
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }else{
                for (int i=0;i<=2;i++) {
                    TERMINAL.puts(InfoCmp.Capability.cursor_address,monitorStartRow+i,0);
                    TERMINAL.puts(InfoCmp.Capability.clr_eol);
                    TERMINAL.writer().print(line[i]);
                }
                TERMINAL.flush();
                TERMINAL.puts(InfoCmp.Capability.cursor_address, size.getRows() - 1, 0);
                READER.callWidget(LineReader.REDRAW_LINE);
                TERMINAL.flush();

            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
