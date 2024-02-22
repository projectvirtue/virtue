package io.github.taikonaut3.virtue.event;

import io.github.taikonaut3.virtue.common.extension.Accessor;

/**
 * Event Object.
 *
 * @param <S> the type of the event source
 */
public interface Event<S> extends Accessor {

    /**
     * Returns the source of the event.
     *
     * @return the event source
     */
    S source();

    /**
     * Sets the source of the event.
     *
     * @param source the event source to set
     */
    void source(S source);

    /**
     * Stops the propagation of the event. Once the propagation is stopped,
     * subsequent listeners will not receive the event.
     */
    void stopPropagation();

    /**
     * Checks if the propagation of the event is stopped.
     *
     * @return true if the propagation is stopped, false otherwise
     */
    boolean isPropagationStopped();

}
