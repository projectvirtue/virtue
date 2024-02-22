package io.github.taikonaut3.virtue.transport.server;

import io.github.taikonaut3.virtue.common.exception.BindException;
import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.NetUtil;
import io.github.taikonaut3.virtue.transport.channel.Channel;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.endpoint.EndpointAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public abstract class AbstractServer extends EndpointAdapter implements Server {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    protected final Channel[] channels;

    protected int soBacklog;

    protected ChannelHandler channelHandler;

    protected Codec codec;

    protected URL url;

    private boolean isInit = false;

    protected AbstractServer(URL url, ChannelHandler channelHandler, Codec codec) throws BindException {
        super(url.host(), url.port());
        this.url = url;
        this.channelHandler = channelHandler;
        this.codec = codec;
        this.channels = channelHandler.getChannels();
        try {
            bind();
            logger.debug("Create {} is Successful,Bind port(s): {} ({})", this.getClass().getSimpleName(), port(), url.protocol());
        } catch (Throwable e) {
            logger.error("Create server fail,Bind port:" + port(), e);
            throw new BindException("Create server fail,Bind address :" + address(), e);
        }
    }

    @Override
    public void bind() throws BindException {
        if (isActive()) {
            return;
        }
        if (!isInit) {
            init();
            isInit = true;
        }
        doBind();
    }

    @Override
    public void close() throws NetWorkException {
        for (Channel channel : channels()) {
            try {
                channel.close();
            } catch (Throwable e) {
                throw new NetWorkException(e);
            }
        }
        try {
            doClose();
        } catch (Throwable e) {
            throw new NetWorkException(e);
        }
    }

    protected abstract void init() throws BindException;

    protected abstract void doBind() throws BindException;

    protected abstract void doClose() throws NetWorkException;

    @Override
    public Channel[] channels() {
        return channels;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        for (Channel channel : channels) {
            if (channel.address().equals(NetUtil.getAddress(remoteAddress))) {
                return channel;
            }
        }
        return null;
    }

}
