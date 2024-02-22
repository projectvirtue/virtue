package io.github.taikonaut3.virtue.transport.endpoint;

import io.github.taikonaut3.virtue.common.util.NetUtil;

import java.net.InetSocketAddress;

/**
 * Network endpoint,including Host and Port information.
 */
public interface Endpoint {

    /**
     * Get the host name of the endpoint.
     *
     * @return host
     */
    String host();

    /**
     * Get the port number of the endpoint.
     *
     * @return port
     */
    int port();

    /**
     * Convert the endpoint to an InetSocketAddress object.
     *
     * @return InetSocketAddress instance
     */
    InetSocketAddress toInetSocketAddress();

    /**
     * Convert the endpoint to "host:port".
     *
     * @return address
     */
    default String address() {
        return NetUtil.getAddress(host(), port());
    }

}