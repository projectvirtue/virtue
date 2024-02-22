package io.github.taikonaut3.virtue.transport.netty.server;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.BindException;
import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.netty.NettyChannel;
import io.github.taikonaut3.virtue.transport.netty.ProtocolInitializer;
import io.github.taikonaut3.virtue.transport.server.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import static io.github.taikonaut3.virtue.common.constant.Constant.DEFAULT_SO_BACKLOG;

public final class NettyServer extends AbstractServer {

    private ServerBootstrap bootstrap;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private Channel channel;

    public NettyServer(URL url, ChannelHandler handler, Codec codec) throws BindException {
        super(url, handler, codec);
    }

    @Override
    protected void init() throws BindException {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(Constant.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyServerWorker", true));
        soBacklog = url.getIntParameter(Key.SO_BACKLOG, DEFAULT_SO_BACKLOG);
        final NettyServerChannelHandler handler = new NettyServerChannelHandler(channelHandler);
        initServerBootStrap(handler);
    }

    private void initServerBootStrap(NettyServerChannelHandler handler) {
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_BACKLOG, soBacklog)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioServerSocketChannel.class)
                .childHandler(ProtocolInitializer.getInitializer(url, handler, codec, true));
    }

    @Override
    protected void doBind() throws BindException {
        ChannelFuture future = bootstrap.bind(port());
        future.syncUninterruptibly();
        channel = future.channel();
    }

    @Override
    protected void doClose() throws NetWorkException {
        try {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            NettyChannel.removeChannel(channel);
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }

    }

    @Override
    public boolean isActive() {
        return channel != null && channel.isActive();
    }

}
