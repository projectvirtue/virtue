package io.github.taikonaut3.virtue.rpc.event;

import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.AbstractEvent;
import io.github.taikonaut3.virtue.transport.Response;
import lombok.Getter;

@Getter
public class ResponseEvent extends AbstractEvent<Response> {

    private final Object body;

    public ResponseEvent(Response response) {
        super(response);
        URL url = response.url();
        Protocol<?,?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
        ProtocolParser protocolParser = protocol.parser();
        this.body = protocolParser.parseResponseBody(response);
    }

}
