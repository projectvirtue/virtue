package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.AbstractEvent;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import lombok.Getter;

@Getter
public class RequestEvent extends AbstractEvent<Request> {

    private final Channel channel;

    private final Object body;

    public RequestEvent(Request request, Channel channel) {
        super(request);
        this.channel = channel;
        URL url = request.url();
        Protocol<?,?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
        ProtocolParser protocolParser = protocol.parser();
        this.body = protocolParser.parseRequestBody(request);
    }

}
