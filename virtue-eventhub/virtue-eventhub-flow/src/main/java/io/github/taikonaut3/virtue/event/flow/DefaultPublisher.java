package io.github.taikonaut3.virtue.event.flow;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultPublisher<T> implements Flow.Publisher<T> {

    private final ConcurrentMap<Flow.Subscriber<? super T>, StandardSubscription<T>> subscriptions = new ConcurrentHashMap<>();

    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    private Executor executor;

    private Queue<T> items = new ConcurrentLinkedQueue<>();

    public DefaultPublisher(Executor executor) {
        this.executor = executor;
    }

    public DefaultPublisher() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        StandardSubscription<T> subscription = new StandardSubscription<>(subscriber, this);
        subscriptions.put(subscriber, subscription);
        subscriber.onSubscribe(subscription);
    }

    public void publish(T item, Flow.Subscriber<? super T> subscriber) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Publisher is already shutdown");
        }
        if (subscriber == null) {
            synchronized (this) {
                items.add(item);
            }
            drain();
        } else {
            StandardSubscription<T> subscription = subscriptions.get(subscriber);
            if (subscription == null || !subscription.isActive()) {
                throw new IllegalArgumentException("Invalid subscriber or inactive subscription");
            }
            executor.execute(() -> subscription.onNext(item));
        }
    }

    public void publish(T item) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Publisher is already shutdown");
        }
        synchronized (this) {
            items.add(item);
        }
        drain();
    }

    public void shutdown() {
        isShutdown.set(true);
        synchronized (this) {
            items.clear();
        }
        for (StandardSubscription<T> subscription : subscriptions.values()) {
            subscription.cancel();
        }
        subscriptions.clear();
    }

    private void drain() {
        executor.execute(() -> {
            boolean needsMore = false;
            T itemToSend = null;
            synchronized (this) {
                if (!items.isEmpty()) {
                    itemToSend = items.poll();
                }
            }
            if (itemToSend != null) {
                for (StandardSubscription<T> subscription : subscriptions.values()) {
                    synchronized (subscription) {
                        if (subscription.isActive()) {
                            subscription.onNext(itemToSend);
                            needsMore = true;
                        }
                    }
                }
            }
            if (needsMore) {
                drain();
            }
        });
    }

    public Executor getExecutor() {
        return executor;
    }

    private static class StandardSubscription<T> implements Flow.Subscription {

        private final Flow.Subscriber<? super T> subscriber;

        private final DefaultPublisher<T> publisher;

        private long demand;

        private boolean cancelled;

        private StandardSubscription(Flow.Subscriber<? super T> subscriber, DefaultPublisher<T> publisher) {
            this.subscriber = subscriber;
            this.publisher = publisher;
        }

        @Override
        public void request(long n) {
            if (n <= 0) {
                cancel();
                subscriber.onError(new IllegalArgumentException("Demand must be positive"));
            } else {
                synchronized (this) {
                    if (cancelled) {
                        return;
                    }
                    demand = Long.MAX_VALUE == demand ? Long.MAX_VALUE : (demand + n < 0 ? Long.MAX_VALUE : (demand + n));
                }
                publisher.drain();
            }
        }

        @Override
        public void cancel() {
            synchronized (this) {
                cancelled = true;
                demand = 0;
            }
            publisher.subscriptions.remove(subscriber);
        }

        public boolean isActive() {
            synchronized (this) {
                return !cancelled && demand > 0;
            }
        }

        private void onNext(T item) {
            long remaining;
            synchronized (this) {
                if (cancelled || demand <= 0) {
                    return;
                }
                subscriber.onNext(item);
                remaining = --demand;
            }
            if (remaining == 0) {
                subscriber.onComplete();
            }
        }

    }

}
