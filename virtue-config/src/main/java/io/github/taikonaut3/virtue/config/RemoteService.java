package io.github.taikonaut3.virtue.config;

/**
 * Remote service.
 *
 * @param <T> The type of the remote service interface.
 */
public interface RemoteService<T> extends CallerContainer {

    /**
     * Gets the target instance of the remote service.
     *
     * @return the target instance
     */
    T target();

    /**
     * Gets the server caller for the specified protocol and path.
     *
     * @param protocol The protocol used for the server caller
     * @param path     The path used for the server caller
     * @return The server caller
     */
    ServerCaller<?> getCaller(String protocol, String path);

    /**
     * Gets the name of the remote service.
     *
     * @return The name of the remote service
     */
    String name();

}

