package io.github.taikonaut3.virtue.transport;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.exception.NetWorkException;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import io.github.taikonaut3.virtue.common.spi.ServiceInterface;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.channel.ChannelHandler;
import io.github.taikonaut3.virtue.transport.client.Client;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.github.taikonaut3.virtue.transport.server.Server;

import static io.github.taikonaut3.virtue.common.constant.Components.Transport.NETTY;

/**
 * Transporter to Bind Server/Connect Client
 */
@ServiceInterface(NETTY)
public interface Transporter {

    AttributeKey<Transporter> ATTRIBUTE_KEY = AttributeKey.get(Key.TRANSPORTER);

    /**
     * Binds a Server to the URL with the given codec.
     *
     * @param url   The URL to bind the server and increase extensibility.
     * @param codec The codec to be used for message encoding and decoding.
     * @return The bound server object.
     * @throws NetWorkException
     */
    Server bind(URL url, ChannelHandler handler, Codec codec) throws NetWorkException;

    /**
     * Connects to the server specified by the URL with the given codec.
     *
     * @param url   The URL of the server to connect and Increase extensibility.
     * @param codec The codec to be used for message encoding and decoding.
     * @return The connected client object.
     * @throws NetWorkException
     */
    Client connect(URL url, ChannelHandler handler, Codec codec) throws NetWorkException;

}
