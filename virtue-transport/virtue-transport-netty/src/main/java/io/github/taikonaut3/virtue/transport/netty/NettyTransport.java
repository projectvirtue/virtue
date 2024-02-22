package io.github.taikonaut3.virtue.transport.netty;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.Transporter;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.client.Client;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.netty.client.NettyClient;
import io.github.taikonaut3.virtue.transport.netty.server.NettyServer;
import io.github.taikonaut3.virtue.transport.server.Server;

import static io.github.taikonaut3.virtue.common.constant.Components.Transport.NETTY;

@ServiceProvider(NETTY)
public final class NettyTransport implements Transporter {

    @Override
    public Client connect(URL url, ChannelHandler handler, Codec codec) {
        return new NettyClient(url, handler, codec);
    }

    @Override
    public Server bind(URL url, ChannelHandler handler, Codec codec) {
        return new NettyServer(url, handler, codec);
    }

}
