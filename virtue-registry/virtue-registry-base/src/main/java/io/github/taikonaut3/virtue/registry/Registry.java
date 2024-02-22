package io.github.taikonaut3.virtue.registry;

import io.github.taikonaut3.virtue.common.exception.ConnectException;
import io.github.taikonaut3.virtue.common.url.URL;

import java.util.List;

public interface Registry {

    /**
     * Checks if the registry is connected.
     *
     * @return true if the registry is connected, false otherwise.
     */
    boolean isAvailable();

    /**
     * Connects to the registry using the specified URL.
     *
     * @param url The URL of the registry to connect to.
     */
    void connect(URL url) throws ConnectException;

    /**
     * Registers the specified URL with the registry.
     *
     * @param url The URL to register.
     */
    void register(URL url);

    /**
     * Discovers services with the specified URL from the registry.
     *
     * @param url The URL used for service discovery.
     * @return A list of URLs representing the discovered services.
     */
    List<URL> discover(URL url);

    /**
     * Destroys the registry.
     */
    void destroy();
}

