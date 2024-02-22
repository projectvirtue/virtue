package io.github.taikonaut3.virtue.transport.netty.custom;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.netty.NettyIdeStateHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Initializes the channel of Netty for Custom Codec
 */
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final URL url;

    private final ChannelHandler handler;

    private final Codec codec;

    private final boolean isServer;

    public CustomChannelInitializer(URL url, ChannelHandler handler, Codec codec, boolean isServer) {
        this.url = url;
        this.handler = handler;
        this.codec = codec;
        this.isServer = isServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        NettyCustomCodec nettyCustomCodec = new NettyCustomCodec(url, codec, isServer);
        int keepAliveTimeout = url.getIntParameter(Key.KEEP_ALIVE_TIMEOUT, Constant.DEFAULT_KEEP_ALIVE_TIMEOUT);
        IdleStateHandler idleStateHandler = NettyIdeStateHandler.create(keepAliveTimeout, isServer);
        socketChannel.pipeline()
                .addLast("decoder", nettyCustomCodec.getDecoder())
                .addLast("encoder", nettyCustomCodec.getEncoder())
                .addLast("heartbeat", idleStateHandler)
                .addLast("handler", handler);
    }
}
