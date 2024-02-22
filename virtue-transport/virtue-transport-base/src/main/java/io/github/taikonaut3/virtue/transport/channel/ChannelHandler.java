package io.github.taikonaut3.virtue.transport.channel;

import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.exception.RpcException;

/**
 * Adaptation network framework.
 */
public interface ChannelHandler {

    /**
     * when a connection is successfully established with the channel.
     *
     * @param channel The channel representing the connection.
     * @throws NetWorkException
     */
    void connected(Channel channel) throws RpcException;

    /**
     * when a connection is disconnected from the channel.
     *
     * @param channel The channel representing the disconnected connection.
     * @throws NetWorkException
     */
    void disconnected(Channel channel) throws RpcException;

    /**
     * when a message is received from the channel.
     *
     * @param channel The channel through which the message was received.
     * @param message The received message.
     * @throws NetWorkException
     */
    void received(Channel channel, Object message) throws RpcException;

    /**
     * when an exception occurs in the channel.
     *
     * @param channel The channel where the exception occurred.
     * @param cause   The cause of the exception.
     * @throws NetWorkException
     */
    void caught(Channel channel, Throwable cause) throws RpcException;

    /**
     * Gets all access Current ChannelHandlerChain's Channel
     *
     * @return
     */
    Channel[] getChannels();

    /**
     * when a heartbeat is received from the channel.
     *
     * @param channel The channel where the heartbeat occurred.
     * @param event
     */
    default void heartBeat(Channel channel, Object event) {

    }
}