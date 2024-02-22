package io.github.taikonaut3.virtue.transport.netty.http;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.netty.NettyIdeStateHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.IdleStateHandler;

import static io.github.taikonaut3.virtue.common.constant.Constant.DEFAULT_MAX_MESSAGE_SIZE;
import static io.github.taikonaut3.virtue.common.constant.Key.CLIENT_MAX_RECEIVE_SIZE;
import static io.github.taikonaut3.virtue.common.constant.Key.MAX_RECEIVE_SIZE;

/**
 * Initializes the channel of Netty for HTTP Codec
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final URL url;

    private final ChannelHandler handler;

    private final boolean isServer;

    public HttpChannelInitializer(URL url, ChannelHandler handler, boolean isServer) {
        this.url = url;
        this.handler = handler;
        this.isServer = isServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        int keepAliveTimeout = url.getIntParameter(Key.KEEP_ALIVE_TIMEOUT, Constant.DEFAULT_KEEP_ALIVE_TIMEOUT);
        IdleStateHandler idleStateHandler = NettyIdeStateHandler.create(keepAliveTimeout, isServer);
        initClientPipeline(socketChannel, idleStateHandler);
    }

    private void initClientPipeline(SocketChannel socketChannel, IdleStateHandler idleStateHandler) {
        String maxMessageKey = isServer ? MAX_RECEIVE_SIZE : CLIENT_MAX_RECEIVE_SIZE;
        int maxReceiveSize = url.getIntParameter(maxMessageKey, DEFAULT_MAX_MESSAGE_SIZE);
        EnvelopeConverter converter = new EnvelopeConverter();
        socketChannel.pipeline()
                .addLast("httpClientCodec", new HttpClientCodec())
                .addLast("requestConverter", converter.requestConverter())
                .addLast("aggregator", new HttpObjectAggregator(maxReceiveSize))
                .addLast("responseConverter", converter.responseConverter())
                .addLast("heartbeat", idleStateHandler)
                .addLast("handler", handler);
    }
}
