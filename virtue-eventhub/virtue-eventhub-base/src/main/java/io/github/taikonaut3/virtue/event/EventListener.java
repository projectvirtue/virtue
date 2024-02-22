package io.github.taikonaut3.virtue.event;

/**
 * Listener for events of type E.
 *
 * @param <E> the type of the event
 */
public interface EventListener<E extends Event<?>> {

    /**
     * This method is called when an event of type E is dispatched.
     *
     * @param event the event that occurred
     */
    void onEvent(E event);

    /**
     * This method is called to check whether the event propagation should continue.
     * By default, it checks if the event's propagation has been stopped.
     * Subclasses can override this method to provide custom logic for event propagation control.
     *
     * @param event the event being checked
     * @return true if event propagation should continue, false otherwise
     */
    default boolean check(E event) {
        return !event.isPropagationStopped();
    }

}

