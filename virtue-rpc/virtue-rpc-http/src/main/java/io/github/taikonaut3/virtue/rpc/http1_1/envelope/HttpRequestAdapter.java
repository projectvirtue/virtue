package io.github.taikonaut3.virtue.rpc.http1_1.envelope;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.taikonaut3.virtue.common.exception.RpcException;
import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.common.url.URL;
import io.github.taikonaut3.virtue.common.util.BeanConverter;
import io.github.taikonaut3.virtue.rpc.http1_1.config.ContentType;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpRequestWrapper;
import io.github.taikonaut3.virtue.serialization.Serializer;
import io.github.taikonaut3.virtue.serialization.json.JacksonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JSON;

/**
 * HttpRequestAdapter base on {@link DefaultFullHttpRequest} convenient convert {@link HttpRequest}
 */
public class HttpRequestAdapter extends DefaultFullHttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestAdapter.class);
    private static final JacksonSerializer jsonSerializer;

    private HttpRequest nettyHttpRequest = this;

    static {
        jsonSerializer = (JacksonSerializer) ExtensionLoader.loadService(Serializer.class, JSON);
    }

    public HttpRequestAdapter(URL url, HttpRequestWrapper wrapper) {
        this(wrapper.httpMethod(), url.pathAndParams());
        String contentType = Optional.ofNullable(wrapper.headers())
                .map(headers -> headers.get(HttpHeaderNames.CONTENT_TYPE.toString()))
                .orElse(ContentType.APPLICATION_JSON);
        this.headers().add(HttpHeaderNames.CONTENT_TYPE, contentType);
        Optional.ofNullable(wrapper.headers()).ifPresent(h -> h.keySet()
                .forEach(key -> this.headers().add(key, h.get(key))));
    }

    public HttpRequestAdapter(String httpMethod, String uri) {
        super(HttpVersion.HTTP_1_1, HttpMethod.valueOf(httpMethod), uri);
    }

    public void body(String contentType, Object body) {
        if (contentType.startsWith(ContentType.MULTIPART_FORM_DATA)) {
            nettyHttpRequest = convertMultipartRequest(body);
        } else {
            ByteBuf content = convertBody(contentType, body);
            nettyHttpRequest = replace(content);
            nettyHttpRequest.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        }
    }

    public HttpRequest nettyHttpRequest() {
        return nettyHttpRequest;
    }

    private HttpRequest convertMultipartRequest(Object body) {
        try {
            HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(this, true);
            Map<String, Object> mapBody = convertToMap(body);
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                if (entry.getValue() instanceof File file) {
                    encoder.addBodyFileUpload(entry.getKey(), file.getName(), file, ContentType.APPLICATION_OCTET_STREAM, false);
                } else {
                    encoder.addBodyAttribute(entry.getKey(), entry.getValue().toString());
                }
            }
            HttpRequest httpRequest = encoder.finalizeRequest();
            if (!encoder.isChunked()) {
                httpRequest.headers().add(HttpHeaderNames.CONTENT_LENGTH, encoder.length());
            }
            return httpRequest;
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            logger.error("Convert convert to MultipartRequest fail", e);
            throw new RpcException("Convert convert to MultipartRequest fail");
        }
    }

    public ByteBuf convertBody(String contentType, Object body) {
        if (contentType.equals(ContentType.APPLICATION_X_WWW_FORM_URLENCODED)) {
            // x-www-form-urlencoded
            Map<String, String> formData = BeanConverter.convertToMap(body);
            String formStr = URL.mapToUrlString(formData);
            return Unpooled.copiedBuffer(formStr, StandardCharsets.UTF_8);
        } else if (contentType.equals(ContentType.APPLICATION_JSON)) {
            // application/json
            byte[] jsonBytes = jsonSerializer.serialize(body);
            return Unpooled.copiedBuffer(jsonBytes);
        } else if (contentType.equals(ContentType.TEXT_PLAIN)) {
            // text/plain
            return Unpooled.copiedBuffer(body.toString(), StandardCharsets.UTF_8);
        } else {
            // other ...
            return Unpooled.EMPTY_BUFFER;
        }
    }

    private Map<String, Object> convertToMap(Object body) {
        ObjectMapper objectMapper = jsonSerializer.objectMapper();
        return objectMapper.convertValue(body, new TypeReference<>() {
        });

    }

}
