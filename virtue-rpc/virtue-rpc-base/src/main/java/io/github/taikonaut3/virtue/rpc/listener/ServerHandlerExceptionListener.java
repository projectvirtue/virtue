package io.github.taikonaut3.virtue.rpc.listener;

import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.event.EventListener;
import io.github.taikonaut3.virtue.rpc.event.ServerHandlerExceptionEvent;
import io.github.taikonaut3.virtue.rpc.protocol.Protocol;
import io.github.taikonaut3.virtue.transport.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandlerExceptionListener implements EventListener<ServerHandlerExceptionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandlerExceptionListener.class);

    @Override
    public void onEvent(ServerHandlerExceptionEvent event) {
        URL url = event.channel().attribute(URL.ATTRIBUTE_KEY).get();
        Throwable cause = event.source();
        logger.error("Server: {} Exception: {}", event.channel(), cause.getMessage());
        if(url!=null){
            Protocol<?,?> protocol = ExtensionLoader.loadService(Protocol.class, url.protocol());
            Object message = protocol.createResponse(url, cause.getMessage());
            Response response = new Response(url, message);
            response.code(Response.ERROR);
            event.channel().send(response);
        }
    }

}
