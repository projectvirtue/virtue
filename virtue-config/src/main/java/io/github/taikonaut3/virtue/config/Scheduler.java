package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;

import java.util.concurrent.TimeUnit;

import static io.github.taikonaut3.virtue.common.constant.Components.DEFAULT;

/**
 * Used for publishing disposable or periodic tasks.
 */
@ServiceInterface(DEFAULT)
public interface Scheduler {

    /**
     * Publishing disposable task.
     *
     * @param runnable
     * @param delay
     * @param unit
     */
    void addDisposable(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Publishing periodic task.
     *
     * @param runnable
     * @param delay
     * @param interval
     * @param unit
     */
    void addPeriodic(Runnable runnable, long delay, long interval, TimeUnit unit);
}
