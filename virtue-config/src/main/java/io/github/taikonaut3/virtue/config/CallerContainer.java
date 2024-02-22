package io.github.taikonaut3.virtue.config;

import io.github.taikonaut3.virtue.config.manager.Virtue;

import java.lang.reflect.Method;

/**
 * The Caller Container For managing the caller
 */
public interface CallerContainer extends Lifecycle {

    /**
     * Gets the name of the microservice application used to locate the target IP.
     *
     * @return application-name
     */
    String remoteApplication();

    /**
     * Gets an array of callers associated with the container.
     *
     * @return all caller instance
     */
    Caller<?>[] callers();

    /**
     * Gets the caller associated with the specified method.
     *
     * @param method the method for which to get the associated caller
     * @return the caller associated with the method, or null if not found
     */
    Caller<?> getCaller(Method method);

    /**
     * Gets the caller associated with the specified identification.
     *
     * @param identification {@link Caller#identification()}
     * @return the caller associated with the identification, or null if not found
     */
    Caller<?> getCaller(String identification);

    /**
     * The Caller container Proxy type
     *
     * @return proxy type
     */
    String proxy();

    /**
     * The belong to virtue
     *
     * @return virtue instance
     */
    Virtue virtue();

}

