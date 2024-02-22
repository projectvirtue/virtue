package io.github.taikonaut3.virtue.event;

import io.github.taikonaut3.virtue.common.extension.AbstractAccessor;

public abstract class AbstractEvent<S> extends AbstractAccessor implements Event<S> {

    protected S data;

    protected volatile boolean propagation = false;

    public AbstractEvent() {

    }

    public AbstractEvent(S data) {
        this.data = data;
    }

    @Override
    public S source() {
        return data;
    }

    @Override
    public void source(S source) {
        this.data = source;
    }

    @Override
    public void stopPropagation() {
        if (!propagation) {
            synchronized (this) {
                if (!propagation) {
                    this.propagation = true;
                }
            }
        }
    }

    @Override
    public boolean isPropagationStopped() {
        return propagation;
    }

}
