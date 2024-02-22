package io.github.taikonaut3.virtue.rpc.virtue.server;

import io.github.taikonaut3.virtue.rpc.handler.RequestChannelHandler;
import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.RpcCallArgs;
import io.github.taikonaut3.virtue.serialization.Converter;
import io.github.taikonaut3.virtue.serialization.Serializer;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.channel.Channel;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JSON;

public class VirtueServerConvertChannelHandler extends RequestChannelHandler {

    @Override
    protected void onSuccess(Channel channel, Request request) {
        URL url = request.url();
        Protocol<?,?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
        ProtocolParser protocolParser = protocol.parser();
        RpcCallArgs callArgs = (RpcCallArgs) protocolParser.parseRequestBody(request);
        String serial = url.getParameter(Key.SERIALIZE);
        if (serial.equals(JSON)) {
            Converter converter = ExtensionLoader.loadService(Serializer.class, JSON);
            Object[] args = converter.convert(callArgs.args(), callArgs.parameterTypes());
            callArgs.args(args);
        }
    }
}
