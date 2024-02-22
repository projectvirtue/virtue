package io.github.taikonaut3.virtue.proxy;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProxyFactory implements ProxyFactory {

    protected final ConcurrentHashMap<String, Object> PROXY_CACHE = new ConcurrentHashMap<>();

    @Override
    public <T> T createProxy(Class<T> interfaceClass, InvocationHandler handler) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(interfaceClass + "Is Not Interface," + "The Method Only Support Interface");
        }
        return doCreateProxy(interfaceClass, handler);
    }

    @Override
    public <T> T createProxy(T target, InvocationHandler handler) {
        return doCreateProxy(target, handler);
    }

    protected abstract <T> T doCreateProxy(Class<T> interfaceClass, InvocationHandler handler);

    protected abstract <T> T doCreateProxy(T target, InvocationHandler handler);

}
