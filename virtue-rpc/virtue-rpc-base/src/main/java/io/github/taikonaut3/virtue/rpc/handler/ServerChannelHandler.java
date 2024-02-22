package io.github.taikonaut3.virtue.rpc.handler;

import io.github.taikonaut3.virtue.rpc.event.RequestEvent;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.base.ChannelHandlerAdapter;
import io.github.taikonaut3.virtue.transport.channel.Channel;

public final class ServerChannelHandler extends ChannelHandlerAdapter {


    @Override
    public void received(Channel channel, Object message) {
        if (message instanceof Request request) {
            getEventDispatcher(request.url()).dispatchEvent(new RequestEvent(request, channel));
        }
    }

}
