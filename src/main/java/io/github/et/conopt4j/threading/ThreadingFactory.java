package io.github.et.conopt4j.threading;

import java.util.concurrent.ThreadFactory;

public class ThreadingFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
