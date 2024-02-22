package io.github.taikonaut3.virtue.transport.netty.client;

import io.github.taikonaut3.virtue.transport.Response;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.netty.NettyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@io.netty.channel.ChannelHandler.Sharable
public final class NettyClientChannelHandler extends SimpleChannelInboundHandler<Response> {

    private final ChannelHandler channelHandler;

    public NettyClientChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getChannel(ctx.channel());
        channelHandler.connected(nettyChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getChannel(ctx.channel());
        channelHandler.disconnected(nettyChannel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getChannel(ctx.channel());
        channelHandler.received(nettyChannel, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getChannel(ctx.channel());
        channelHandler.caught(nettyChannel, cause);
        NettyChannel.removeChannel(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        NettyChannel nettyChannel = NettyChannel.getChannel(ctx.channel());
        if (evt instanceof IdleStateEvent event) {
            if ((event.state() == IdleState.ALL_IDLE) || (event.state() == IdleState.WRITER_IDLE)) {
                channelHandler.heartBeat(nettyChannel, evt);
            }
        }
    }

}
