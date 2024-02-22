package io.github.taikonaut3.virtue.transport.server;

import io.github.taikonaut3.virtue.common.exception.BindException;
import io.github.taikonaut3.virtue.transport.Closeable;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import io.github.taikonaut3.virtue.transport.endpoint.Endpoint;

import java.net.InetSocketAddress;

/**
 * Net server
 */
public interface Server extends Endpoint, Closeable {

    /**
     * Binds the server to the specified host and port.
     *
     * @throws BindException if an error occurs during binding.
     */
    void bind() throws BindException;

    /**
     * Returns all channels associated with the server.
     *
     * @return An array of channels associated with the server.
     */
    Channel[] channels();

    /**
     * Returns the channel associated with the specified remote address.
     *
     * @param remoteAddress The remote address to get the associated channel for.
     * @return The channel associated with the specified remote address.
     */
    Channel getChannel(InetSocketAddress remoteAddress);

}
