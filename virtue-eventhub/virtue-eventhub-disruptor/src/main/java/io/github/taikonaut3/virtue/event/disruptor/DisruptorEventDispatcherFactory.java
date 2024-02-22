package io.github.taikonaut3.virtue.event.disruptor;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.SingleServiceFactory;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.EventDispatcher;
import io.github.taikonaut3.virtue.event.EventDispatcherFactory;

import static io.github.taikonaut3.virtue.common.constant.Components.EventDispatcher.DISRUPTOR;

/**
 * DisruptorEventDispatcherFactory
 */
@ServiceProvider(DISRUPTOR)
public class DisruptorEventDispatcherFactory extends SingleServiceFactory<EventDispatcher> implements EventDispatcherFactory {

    @Override
    protected EventDispatcher create(URL url) {
        return new DisruptorEventDispatcher(url);
    }
}
