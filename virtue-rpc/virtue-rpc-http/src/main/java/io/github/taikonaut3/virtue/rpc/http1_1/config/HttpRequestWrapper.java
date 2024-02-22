package io.github.taikonaut3.virtue.rpc.http1_1.config;

import io.github.taikonaut3.virtue.common.constant.Key;
import io.github.taikonaut3.virtue.common.extension.AttributeKey;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class HttpRequestWrapper {

    public static final AttributeKey<HttpRequestWrapper> ATTRIBUTE_KEY = AttributeKey.get(Key.HTTP_REQUEST_WRAPPER);

    private Method method;

    private String path;

    private String httpMethod;

    private Map<String, String> headers;

    private Map<String, String> params;

    private Object body;

    public HttpRequestWrapper() {
        headers = new HashMap<>();
        params = new HashMap<>();

    }

    public HttpRequestWrapper(Method method) {
        this();
        this.method = method;
    }

    public HttpRequestWrapper deepCopy() {
        HttpRequestWrapper wrapper = new HttpRequestWrapper(this.method);
        wrapper.path(this.path);
        wrapper.httpMethod(this.httpMethod);
        wrapper.headers(this.headers);
        wrapper.params(this.params);
        return wrapper;
    }

}
