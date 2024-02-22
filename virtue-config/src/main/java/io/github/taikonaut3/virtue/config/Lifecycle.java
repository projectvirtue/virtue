package io.github.taikonaut3.virtue.config;

/**
 * Lifecycle interface that specifies the initialization, startup, and stop methods for a component.
 */
public interface Lifecycle {

    /**
     * Initializes the component. This method is typically called when the component is created.
     */
    default void init() {

    }

    /**
     * Starts the component. This method is typically called when the component is ready to run.
     */
    default void start() {

    }

    /**
     * Stops the component. This method is typically called when the component is shutting down.
     */
    default void stop() {

    }

}

