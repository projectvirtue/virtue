package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.transport.channel.Channel;

public class ClientHandlerExceptionEvent extends ChannelHandlerExceptionEvent {

    public ClientHandlerExceptionEvent(Channel channel, Throwable cause) {
        super(channel, cause);
    }

}
