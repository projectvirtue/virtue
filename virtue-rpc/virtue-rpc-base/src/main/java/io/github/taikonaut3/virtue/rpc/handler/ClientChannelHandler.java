package io.github.taikonaut3.virtue.rpc.handler;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.rpc.event.ResponseEvent;
import io.github.taikonaut3.virtue.transport.Response;
import io.github.taikonaut3.virtue.transport.base.ChannelHandlerAdapter;
import io.github.taikonaut3.virtue.transport.channel.Channel;

public final class ClientChannelHandler extends ChannelHandlerAdapter {

    @Override
    public void received(Channel channel, Object message) throws RpcException {
        if (message instanceof Response response) {
            getEventDispatcher(response.url()).dispatchEvent(new ResponseEvent(response));
        }
    }

}
