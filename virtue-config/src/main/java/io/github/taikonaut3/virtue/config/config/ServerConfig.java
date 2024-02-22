package io.github.taikonaut3.virtue.config.config;

import io.github.taikonaut3.virtue.common.constant.Constant;
import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.Parameter;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Server Config
 */
@Accessors(fluent = true, chain = true)
@Getter
@Setter
public class ServerConfig extends UrlTypeConfig {

    @Parameter(Key.USERNAME)
    private String username;

    @Parameter(Key.PASSWORD)
    private String password;

    @Parameter(Key.SSL)
    private boolean ssl;

    @Parameter(Key.MAX_CONNECTIONS)
    private int maxConnections;

    @Parameter(Key.MAX_THREADS)
    private int maxThreads;

    @Parameter(Key.MAX_RECEIVE_SIZE)
    private int maxMessageSize = Constant.DEFAULT_MAX_MESSAGE_SIZE;

    @Parameter(Key.MAX_CONNECTIONS)
    private int keepAliveTimeout;

    public ServerConfig() {

    }

    public ServerConfig(String type) {
        type(type);
        name(type);
    }

    public ServerConfig(String type, int port) {
        this(type);
        port(port);
    }

    @Override
    public URL toUrl() {
        return new URL(type, NetUtil.getLocalHost(), port, parameterization());
    }

    @Override
    public String toString() {
        return toUrl().toString();
    }

}
