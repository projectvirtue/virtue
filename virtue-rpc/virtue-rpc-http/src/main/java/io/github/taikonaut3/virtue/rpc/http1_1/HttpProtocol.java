package io.github.taikonaut3.virtue.rpc.http1_1;

import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.rpc.handler.*;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpRequestWrapper;
import io.github.taikonaut3.virtue.rpc.http1_1.envelope.HttpRequestAdapter;
import io.github.taikonaut3.virtue.rpc.objectfactory.ClientPool;
import io.github.taikonaut3.virtue.rpc.protocol.AbstractProtocol;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.client.Client;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.HTTP;
import static io.github.taikonaut3.virtue.rpc.http1_1.config.HttpMethod.*;

/**
 * HttpProtocol
 */
@ServiceProvider(HTTP)
public class HttpProtocol extends AbstractProtocol<HttpRequest, FullHttpResponse> {

    public static final Logger logger = LoggerFactory.getLogger(HttpProtocol.class);

    public static final List<String> NO_BODY_METHODS = List.of(GET, HEAD, DELETE, OPTIONS, TRACE);

    private final Map<String, ClientPool> clientPools = new ConcurrentHashMap<>();

    private final HttpParser httpParser;

    public HttpProtocol() {
        super(HTTP,
                null,
                null,
                clientChannelHandler(),
                serverChannelHandler(),
                new HttpParser());
        httpParser = (HttpParser) protocolParser;
    }

    @Override
    public Client openClient(URL url) {
        String key = url.address();
        ClientPool clientPool;
        clientPool = clientPools.get(key);
        if (clientPool == null) {
            synchronized (this) {
                if (clientPools.get(key) == null) {
                    clientPool = new ClientPool(transporter, clientCodec, clientHandler);
                    clientPools.put(key, clientPool);
                }
            }
        }
        assert clientPool != null;
        return clientPool.get(url);
    }

    @Override
    public HttpRequest createRequest(URL url, CallArgs args) {
        try {
            HttpRequestWrapper wrapper = url.attribute(HttpRequestWrapper.ATTRIBUTE_KEY).get();
            HttpRequestAdapter httpRequestAdapter = new HttpRequestAdapter(url, wrapper);
            Object body = null;
            if (!NO_BODY_METHODS.contains(wrapper.httpMethod())) {
                body = httpParser.methodParser().parseRequestBody(args, wrapper);
            }
            if (body != null) {
                String contentType = httpRequestAdapter.headers().get(HttpHeaderNames.CONTENT_TYPE);
                httpRequestAdapter.body(contentType, body);
            }
            return httpRequestAdapter.nettyHttpRequest();
        } catch (Exception e) {
            logger.error("Create Http Request fail", e);
            throw new RpcException(e);
        }

    }

    @Override
    public FullHttpResponse createResponse(URL url, Object payload) {
        return null;
    }

    public void returnClient(Client client) {
        String address = client.address();
        ClientPool clientPool = clientPools.get(address);
        clientPool.returnClient(client);
    }

    private static ChannelHandler clientChannelHandler() {
        DefaultChannelHandlerChain handlerChain = new DefaultChannelHandlerChain();
        handlerChain
                .addLast(new ClientHeartBeatChannelHandler())
                .addLast(new ResponseChannelHandler())
                .addLast(new ClientChannelHandler());
        return handlerChain;
    }

    private static ChannelHandler serverChannelHandler() {
        DefaultChannelHandlerChain handlerChain = new DefaultChannelHandlerChain();
        handlerChain
                .addLast(new ServerHeartBeatChannelHandler())
                .addLast(new RequestChannelHandler())
                .addLast(new ServerChannelHandler());
        return handlerChain;
    }
}
