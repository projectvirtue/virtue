package io.github.taikonaut3.virtue.rpc.virtue.server;

import io.github.taikonaut3.virtue.rpc.handler.DefaultChannelHandlerChain;
import io.github.taikonaut3.virtue.rpc.handler.ServerChannelHandler;
import io.github.taikonaut3.virtue.rpc.handler.ServerHeartBeatChannelHandler;

/**
 * +---------------------------------------ServerChannelHandlerChain---------------------------------------+
 * +--------------------------------+--------------------------------------+-------------------------------+
 * |{@link ServerHeartBeatChannelHandler} -> {@link virtueServerConvertChannelHandler} -> {@link ServerChannelHandler} |
 * +--------------------------------+--------------------------------------+-------------------------------+
 */
public class VirtueServerChannelHandlerChain extends DefaultChannelHandlerChain {

    public VirtueServerChannelHandlerChain() {
        addLast(new ServerHeartBeatChannelHandler());
        addLast(new VirtueServerConvertChannelHandler());
        addLast(new ServerChannelHandler());
    }
}
