package io.github.taikonaut3.virtue.transport.netty.custom;

import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Getter;

import static io.github.taikonaut3.virtue.common.constant.Constant.DEFAULT_MAX_MESSAGE_SIZE;
import static io.github.taikonaut3.virtue.common.constant.Key.CLIENT_MAX_RECEIVE_SIZE;
import static io.github.taikonaut3.virtue.common.constant.Key.MAX_RECEIVE_SIZE;

/**
 * Read the Envelope by data length
 */
public final class NettyCustomCodec {

    @Getter
    private final ChannelHandler encoder;

    @Getter
    private final ChannelHandler decoder;

    private final Codec codec;

    public NettyCustomCodec(URL url, Codec codec, boolean isServer) {
        this.codec = codec;
        String maxMessageKey = isServer ? MAX_RECEIVE_SIZE : CLIENT_MAX_RECEIVE_SIZE;
        int maxReceiveSize = url.getIntParameter(maxMessageKey, DEFAULT_MAX_MESSAGE_SIZE);
        encoder = new NettyEncoder();
        decoder = new NettyDecoder(maxReceiveSize);
    }

    class NettyEncoder extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
            byte[] bytes = codec.encode(message);
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }

    }

    class NettyDecoder extends LengthFieldBasedFrameDecoder {

        public NettyDecoder(int maxFrameLength) {
            super(maxFrameLength, 0, 4, 0, 4, true);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
            if (byteBuf != null) {
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                return codec.decode(bytes);
            }
            return null;
        }

    }

}
