package io.github.taikonaut3.virtue.transport.endpoint;

import java.net.InetSocketAddress;

public class EndpointAdapter implements Endpoint {

    protected InetSocketAddress address;

    public EndpointAdapter(InetSocketAddress address) {
        this.address = address;
    }

    protected EndpointAdapter(String ip, int port) {
        this.address = new InetSocketAddress(ip, port);
    }

    @Override
    public String host() {
        return address.getHostString();
    }

    @Override
    public int port() {
        return address.getPort();
    }

    @Override
    public InetSocketAddress toInetSocketAddress() {
        return address;
    }

}
