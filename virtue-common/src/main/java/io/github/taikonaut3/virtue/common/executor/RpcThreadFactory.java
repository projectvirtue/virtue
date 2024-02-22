package io.github.taikonaut3.virtue.common.executor;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final ThreadExceptionHandler threadExceptionHandler = new ThreadExceptionHandler();

    private final String namePrefix;

    private final boolean isDaemon;

    public RpcThreadFactory(String namePrefix, boolean isDaemon) {
        this.namePrefix = namePrefix;
        this.isDaemon = isDaemon;
    }

    public RpcThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
        this.isDaemon = false;
    }

    @Override
    public Thread newThread(Runnable r) {
        Objects.requireNonNull(r);
        Thread t = new Thread(r, "virtue-" + namePrefix + "-" + threadNumber.getAndIncrement());
        t.setUncaughtExceptionHandler(threadExceptionHandler);
        t.setDaemon(isDaemon);
        return t;
    }

}