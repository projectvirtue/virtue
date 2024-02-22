package io.github.taikonaut3.virtue.config.config;

import io.github.taikonaut3.virtue.common.url.Parameterization;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;

@Accessors(fluent = true, chain = true)
@Getter
@Setter
public abstract class UrlTypeConfig implements Parameterization {

    protected String type;

    protected String name;

    protected String host;

    protected int port;

    public abstract URL toUrl();

    public String getAddress() {
        return NetUtil.getAddress(host, port);
    }

    public UrlTypeConfig address(String address) {
        InetSocketAddress inetSocketAddress = NetUtil.toInetSocketAddress(address);
        this.host = inetSocketAddress.getHostString();
        this.port = inetSocketAddress.getPort();
        return this;
    }

    @Override
    public String toString() {
        return toUrl().toString();
    }
}
