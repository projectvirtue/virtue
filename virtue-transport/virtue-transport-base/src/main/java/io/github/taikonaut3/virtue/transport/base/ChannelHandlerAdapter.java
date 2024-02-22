package io.github.taikonaut3.virtue.transport.base;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import io.github.taikonaut3.virtue.event.EventDispatcher;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;

public class ChannelHandlerAdapter implements ChannelHandler {



    @Override
    public void connected(Channel channel) throws RpcException {

    }

    @Override
    public void disconnected(Channel channel) throws RpcException {

    }

    @Override
    public void received(Channel channel, Object message) throws RpcException {

    }

    @Override
    public void caught(Channel channel, Throwable cause) throws RpcException {

    }

    @Override
    public Channel[] getChannels() {
        return new Channel[0];
    }

    public EventDispatcher getEventDispatcher(URL url) {
        Virtue virtue = Virtue.get(url);
        return virtue.eventDispatcher();
    }
}
