package io.github.taikonaut3.virtue.config;

/**
 * Remote caller for invoking remote service methods.
 *
 * @param <T> The type of the remote service interface.
 */
public interface RemoteCaller<T> extends CallerContainer {

    /**
     * Gets the target interface of the remote service.
     *
     * @return the target interface
     */
    Class<T> targetInterface();

    /**
     * Gets a Proxy instance of the remote service.
     *
     * @return proxy instance
     */
    T get();

    /**
     * Is it only when the first call is made that the registration center is actually connected to Get the available services.
     * The default gets available services when {@link io.github.taikonaut3.virtue.config.ClientCaller} creation is complete.
     */
    boolean lazyDiscover();

}

