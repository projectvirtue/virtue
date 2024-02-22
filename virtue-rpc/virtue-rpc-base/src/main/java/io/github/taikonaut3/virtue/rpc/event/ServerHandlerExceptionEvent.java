package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.transport.channel.Channel;

public class ServerHandlerExceptionEvent extends ChannelHandlerExceptionEvent {

    public ServerHandlerExceptionEvent(Channel channel, Throwable cause) {
        super(channel, cause);
    }

}
