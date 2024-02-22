package io.github.taikonaut3.virtue.rpc.handler;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.exception.SourceException;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.RpcCallArgs;
import io.github.taikonaut3.virtue.config.ServerCaller;
import io.github.taikonaut3.virtue.config.manager.Virtue;
import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.base.ChannelHandlerAdapter;
import io.github.taikonaut3.virtue.transport.channel.Channel;

public class RequestChannelHandler extends ChannelHandlerAdapter {

    @Override
    public void received(Channel channel, Object message) throws RpcException {
        if (message instanceof Request request) {
            URL url = request.url();
            Virtue virtue = Virtue.get(url);
            Protocol<?, ?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
            ProtocolParser protocolParser = protocol.parser();
            RpcCallArgs callArgs = (RpcCallArgs) protocolParser.parseRequestBody(request);
            ServerCaller<?> serverCaller = virtue.configManager().remoteServiceManager().getServerCaller(url.protocol(), url.path());
            if (serverCaller == null) {
                throw new SourceException("Can't find  ProviderCaller[" + url.path() + "]");
            }
            callArgs.caller(serverCaller);
            callArgs.returnType(serverCaller.method().getGenericReturnType());
            callArgs.parameterTypes(serverCaller.method().getGenericParameterTypes());
            onSuccess(channel, request);
        }
    }

    protected void onSuccess(Channel channel, Request request) {

    }

}
