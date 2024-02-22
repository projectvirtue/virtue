package io.github.taikonaut3.virtue.transport.netty;

import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyIdeStateHandler extends IdleStateHandler {

    protected NettyIdeStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.MILLISECONDS);
    }

    protected static IdleStateHandler createServerIdleStateHandler(int heartbeatInterval) {
        return new NettyIdeStateHandler(heartbeatInterval, 0, heartbeatInterval);
    }

    protected static IdleStateHandler createClientIdleStateHandler(int heartbeatInterval) {
        return new NettyIdeStateHandler(0, heartbeatInterval, heartbeatInterval);
    }

    public static IdleStateHandler create(int heartbeatInterval, boolean isServer) {
        return isServer ? createServerIdleStateHandler(heartbeatInterval) : createClientIdleStateHandler(heartbeatInterval);
    }

}
