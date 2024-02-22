package io.github.taikonaut3.virtue.common.executor;

import io.github.taikonaut3.virtue.common.constant.Constant;

import java.util.concurrent.*;

public class RpcThreadPool extends ThreadPoolExecutor {

    public RpcThreadPool(int corePoolSize, int maximumPoolSize, String namePrefix) {
        super(corePoolSize, maximumPoolSize,
                Constant.DEFAULT_KEEPALIVE, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(Constant.DEFAULT_CAPACITY),
                new RpcThreadFactory(namePrefix, false),
                new CallerRunsPolicy());
    }

    public RpcThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public RpcThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public RpcThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public RpcThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ExecutorService defaultIOExecutor(String namePrefix) {
        return new RpcThreadPool(Constant.DEFAULT_IO_THREADS, Constant.DEFAULT_IO_MAX_THREADS, namePrefix);
    }

    public static ExecutorService defaultCPUExecutor(String namePrefix) {
        return new RpcThreadPool(Constant.DEFAULT_CPU_THREADS, Constant.DEFAULT_CPU_MAX_THREADS, namePrefix);
    }

}
