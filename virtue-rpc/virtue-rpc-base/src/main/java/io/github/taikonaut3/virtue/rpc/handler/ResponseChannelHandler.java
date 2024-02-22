package io.github.taikonaut3.virtue.rpc.handler;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.rpc.RpcFuture;
import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.transport.Response;
import io.github.taikonaut3.virtue.transport.base.ChannelHandlerAdapter;
import io.github.taikonaut3.virtue.transport.channel.Channel;

/**
 * Response status check
 */
public class ResponseChannelHandler extends ChannelHandlerAdapter {

    @Override
    public void received(Channel channel, Object message) throws RpcException {
        if (message instanceof Response response) {
            RpcFuture future = RpcFuture.getFuture(String.valueOf(response.id()));
            // if timeout the future will is null
            if (future != null) {
                if (response.code() == Response.ERROR) {
                    // throw server handle Exception -> ChannelHandlerExceptionEventListener
                    onError(channel, response);
                } else if (response.code() == Response.SUCCESS) {
                    onSuccess(channel, response);
                }
            }
        }
    }

    protected void onSuccess(Channel channel, Response response) {

    }

    protected void onError(Channel channel, Response response) {
        URL url = response.url();
        Protocol<?,?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
        ProtocolParser protocolParser = protocol.parser();
        Object body = protocolParser.parseResponseBody(response);
        throw new RpcException(String.valueOf(body));
    }
}
