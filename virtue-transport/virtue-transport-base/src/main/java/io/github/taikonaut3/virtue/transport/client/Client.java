package io.github.taikonaut3.virtue.transport.client;

import io.github.taikonaut3.virtue.common.exception.ConnectException;
import io.github.taikonaut3.virtue.transport.Closeable;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import io.github.taikonaut3.virtue.transport.endpoint.Endpoint;

/**
 * Net client
 */
public interface Client extends Endpoint, Closeable {

    /**
     * Connects the client to the server.
     *
     * @throws ConnectException if an error occurs during connection.
     */
    void connect() throws ConnectException;

    /**
     * Returns the channel associated with the client.
     *
     * @return The channel associated with the client.
     */
    Channel channel();

    /**
     * Sends a message using the client's channel.
     *
     * @param message The message to send.
     */
    default void send(Object message) {
        channel().send(message);
    }

}
