package io.github.taikonaut3.virtue.rpc.http1_1;

import io.github.taikonaut3.virtue.common.spi.ExtensionLoader;
import io.github.taikonaut3.virtue.config.CallArgs;
import io.github.taikonaut3.virtue.config.Caller;
import io.github.taikonaut3.virtue.rpc.RpcFuture;
import io.github.taikonaut3.virtue.rpc.http1_1.config.DefaultWebMethodParser;
import io.github.taikonaut3.virtue.rpc.http1_1.config.MethodParser;
import io.github.taikonaut3.virtue.rpc.protocol.ProtocolParser;
import io.github.taikonaut3.virtue.serialization.Serializer;
import io.github.taikonaut3.virtue.transport.Request;
import io.github.taikonaut3.virtue.transport.Response;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.taikonaut3.virtue.common.constant.Components.Serialize.JSON;

/**
 * HttpParser
 */
@Data
@Accessors(fluent = true)
public class HttpParser implements ProtocolParser {

    private MethodParser<?> methodParser = new DefaultWebMethodParser(this);

    @Override
    public CallArgs parseRequestBody(Request request) {
        return null;
    }

    @Override
    public Object parseResponseBody(Response response) {
        FullHttpResponse httpResponse = (FullHttpResponse) response.message();
        ByteBuf byteBuf = httpResponse.content();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        Serializer serializer = ExtensionLoader.loadService(Serializer.class, JSON);
        RpcFuture future = RpcFuture.getFuture(String.valueOf(response.id()));
        Caller<?> caller = future.callArgs().caller();
        Type type = caller.returnType();
        Object object = serializer.deserialize(bytes, caller.returnClass());
        return serializer.convert(object, type);
    }

    public Map<String, String> parseHeaders(String[] headers) {
        return getStringMap(headers);
    }

    public Map<String, String> parseParams(String[] params) {
        return getStringMap(params);
    }

    private Map<String, String> getStringMap(String[] params) {
        if (params == null || params.length == 0) {
            return new HashMap<>();
        }
        return Arrays.stream(params)
                .map(pair -> pair.split("="))
                .filter(keyValue -> keyValue.length == 2)
                .collect(Collectors.toMap(
                        keyValue -> keyValue[0].trim(),
                        keyValue -> keyValue[1].trim()
                ));
    }

}
