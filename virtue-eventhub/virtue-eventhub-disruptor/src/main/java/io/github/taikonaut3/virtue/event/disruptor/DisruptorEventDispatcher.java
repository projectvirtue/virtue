package io.github.taikonaut3.virtue.event.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.executor.RpcThreadFactory;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.AbstractEventDispatcher;
import io.github.taikonaut3.virtue.event.Event;
import io.github.taikonaut3.virtue.event.EventListener;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DisruptorEventDispatcher extends AbstractEventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorEventDispatcher.class);

    private final int bufferSize;

    private RingBuffer<EventHolder<?>> ringBuffer;

    public DisruptorEventDispatcher(URL url) {
        this.bufferSize = url.getIntParameter(Key.BUFFER_SIZE, Constant.DEFAULT_BUFFER_SIZE);
        createDispatcher();
    }

    private void createDispatcher() {
        Disruptor<EventHolder<?>> disruptor = new Disruptor<>(EventHolder::new, bufferSize,
                new RpcThreadFactory("event-disruptor"));
        this.ringBuffer = disruptor.getRingBuffer();
        disruptor.handleEventsWith(this::handleEvent);
        disruptor.setDefaultExceptionHandler(new ExceptionHandler<>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, EventHolder<?> event) {
                logger.error(this.getClass().getSimpleName() + " Handle Event Error", ex);
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                logger.error(this.getClass().getSimpleName() + " Start Error", ex);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                logger.error(this.getClass().getSimpleName() + " Shutdown Error", ex);
            }
        });
        disruptor.start();
    }

    @Override
    public <E extends Event<?>> void addListener(Class<E> eventType, EventListener<E> listener) {
        super.addListener(eventType, listener);
        logger.debug("Register Listener[{}] listen Event[{}]", listener.getClass().getSimpleName(), eventType.getSimpleName());
    }

    @Override
    public <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener) {
        super.removeListener(eventType, listener);
        logger.debug("Remove Listener[{}] listen Event[{}]", listener.getClass().getSimpleName(), eventType.getSimpleName());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <E extends Event<?>> void doDispatchEvent(E event) {
        ringBuffer.publishEvent((eventHolder, sequence) -> {
            EventHolder<E> holder = (EventHolder<E>) eventHolder;
            holder.setEvent(event);
        });
        logger.trace("DispatchEvent ({})", event.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <E extends Event<?>> void handleEvent(EventHolder<E> holder, long sequence, boolean endOfBatch) {
        E event = holder.getEvent();
        List<EventListener<?>> listeners = listenerMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(event.getClass()))
                .flatMap(entry -> entry.getValue().stream())
                .toList();
        for (EventListener<?> item : listeners) {
            EventListener<E> listener = (EventListener<E>) item;
            if (listener.check(event)) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    logger.error("Handle Failed Event(" + event.getClass().getSimpleName() + ") current Listener ", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Getter
    private static class EventHolder<E extends Event<?>> {

        private E event;

        public void setEvent(E event) {
            this.event = event;
        }

    }

}

