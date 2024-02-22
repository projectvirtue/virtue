package io.github.taikonaut3.virtue.transport.channel;

import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.extension.Accessor;
import io.github.taikonaut3.virtue.transport.Closeable;
import io.github.taikonaut3.virtue.transport.endpoint.Endpoint;

import java.net.InetSocketAddress;

/**
 * Net channel interface that extends the Endpoint and Closeable interfaces.
 */
public interface Channel extends Endpoint, Accessor, Closeable {

    /**
     * Returns the remote address associated with the channel.
     *
     * @return The remote address associated with the channel.
     */
    InetSocketAddress remoteAddress();

    /**
     * Sends the given message through the channel.
     *
     * @param message The message to send.
     * @throws NetWorkException if an error occurs during network communication.
     */
    void send(Object message) throws NetWorkException;

}
