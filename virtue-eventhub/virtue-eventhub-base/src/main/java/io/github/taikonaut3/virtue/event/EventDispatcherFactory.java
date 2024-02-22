package io.github.taikonaut3.virtue.event;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.common.url.ServiceFactory;

import static io.github.taikonaut3.virtue.common.constant.Components.EventDispatcher.DISRUPTOR;

/**
 * Factory for creating event dispatchers.
 */
@ServiceInterface(DISRUPTOR)
public interface EventDispatcherFactory extends ServiceFactory<EventDispatcher> {

}

