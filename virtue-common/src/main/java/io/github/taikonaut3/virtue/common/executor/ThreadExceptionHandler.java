package io.github.taikonaut3.virtue.common.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ThreadExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error(t.getName() + "-" + t.threadId() + " have a Error", e);
    }

}
