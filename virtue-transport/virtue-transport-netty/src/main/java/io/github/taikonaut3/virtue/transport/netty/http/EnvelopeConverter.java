package io.github.taikonaut3.virtue.transport.netty.http;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.Response;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * EnvelopeConverter
 */
@Accessors(fluent = true)
@Getter
public final class EnvelopeConverter {

    private final ChannelHandler requestConverter = new RequestConverter();

    private final ChannelHandler responseConverter = new ResponseConverter();

    private static final AttributeKey<URL> urlKey = AttributeKey.newInstance(Key.URL);

    static class RequestConverter extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (msg instanceof Request request) {
                HttpRequest httpRequest = (HttpRequest) request.message();
                httpRequest.headers()
                        .add(HttpHeaderNames.HOST, request.url().address())
                        .add(HttpHeaderNames.USER_AGENT, "Netty HttpClient")
                        .add(Key.URL, request.url());
                msg = httpRequest;
                ctx.channel().attr(urlKey).set(request.url());
            }
            ctx.write(msg, promise);
        }
    }

    static class ResponseConverter extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof FullHttpResponse httpResponse) {
                URL url = ctx.channel().attr(urlKey).get();
                msg = new Response(url, httpResponse);
            }
            ctx.fireChannelRead(msg);
        }
    }

}
