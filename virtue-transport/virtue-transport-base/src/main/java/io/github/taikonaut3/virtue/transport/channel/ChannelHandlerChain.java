package io.github.taikonaut3.virtue.transport.channel;

/**
 * Connect the ChannelHandler in order
 */
public interface ChannelHandlerChain extends ChannelHandler {

    /**
     * Just the addition of the Inbound ChannelHandler
     *
     * @param channelHandler
     */
    ChannelHandlerChain addLast(ChannelHandler channelHandler);

    /**
     * Gets all Inbound ChannelHandler
     *
     * @return
     */
    ChannelHandler[] channelHandlers();

}
