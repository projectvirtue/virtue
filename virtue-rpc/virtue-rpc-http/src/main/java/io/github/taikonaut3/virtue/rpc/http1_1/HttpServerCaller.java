package io.github.taikonaut3.virtue.rpc.http1_1;

import io.github.taikonaut3.virtue.config.RemoteService;
import io.github.taikonaut3.virtue.config.annotation.Config;
import io.github.taikonaut3.virtue.rpc.config.AbstractServerCaller;
import io.github.taikonaut3.virtue.rpc.http1_1.config.HttpCallable;

import java.lang.reflect.Method;
import java.util.List;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.HTTP;

public class HttpServerCaller extends AbstractServerCaller<HttpCallable> {

    public HttpServerCaller(Method method, RemoteService<?> remoteService) {
        super(method, remoteService, HTTP, HttpCallable.class);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String path() {
        return parsedAnnotation.path();
    }

    @Override
    public List<String> pathList() {
        return List.of(parsedAnnotation.path());
    }

    @Override
    protected Config config() {
        return parsedAnnotation.config();
    }

}
