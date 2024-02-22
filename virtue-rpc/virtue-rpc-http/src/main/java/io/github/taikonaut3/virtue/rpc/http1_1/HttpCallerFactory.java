package io.github.taikonaut3.virtue.rpc.http1_1;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.config.*;

import java.lang.reflect.Method;

import static io.github.taikonaut3.virtue.common.constant.Components.Protocol.HTTP;

@ServiceProvider(HTTP)
public class HttpCallerFactory implements CallerFactory {
    @Override
    public ServerCaller<?> createServerCaller(Method method, RemoteService<?> remoteService) {
        return new HttpServerCaller(method, remoteService);
    }

    @Override
    public ClientCaller<?> createClientCaller(Method method, RemoteCaller<?> remoteCaller) {
        return new HttpClientCaller(method, remoteCaller);
    }
}
