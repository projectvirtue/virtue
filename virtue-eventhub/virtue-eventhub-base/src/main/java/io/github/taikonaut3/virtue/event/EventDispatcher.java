package io.github.taikonaut3.virtue.event;

/**
 * Event dispatcher that can register, remove,and dispatch event listeners for specific types of events.
 */
public interface EventDispatcher {

    /**
     * Registers an event listener for the specified event type. The listener will receive
     * all events of the specified or subclass type  that are dispatched by this dispatcher.
     *
     * @param eventType the class object representing the event type
     * @param listener  the event listener to register
     * @param <E>       the type of the event
     */
    <E extends Event<?>> void addListener(Class<E> eventType, EventListener<E> listener);

    /**
     * Removes an event listener for the specified event type. If the listener is not currently
     * registered, this method has no effect.
     *
     * @param eventType the class object representing the event type
     * @param listener  the event listener to remove
     * @param <E>       the type of the event
     */
    <E extends Event<?>> void removeListener(Class<E> eventType, EventListener<E> listener);

    /**
     * Dispatches the specified event to all registered listeners for the event type of the
     * event. The order in which the listeners receive the event is undefined.
     *
     * @param event the event to dispatch
     * @param <E>   the type of the event
     */
    <E extends Event<?>> void dispatchEvent(E event);

}
