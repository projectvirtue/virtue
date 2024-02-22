package io.github.taikonaut3.virtue.proxy;

import io.github.taikonaut3.virtue.common.spi.ServiceInterface;

import static io.github.taikonaut3.virtue.common.constant.Components.ProxyFactory.JDK;

/**
 * Factory for creating proxies.
 */
@ServiceInterface(JDK)
public interface ProxyFactory {

    /**
     * Creates a new proxy instance implementing the specified interface using the given invocation handler.
     *
     * @param interfaceClass the interface to be implemented by the proxy object
     * @param handler        the invocation handler to dispatch method invocations to
     * @return a new proxy object that implements the specified interface
     */
    <T> T createProxy(Class<T> interfaceClass, InvocationHandler handler);

    /**
     * Creates a new proxy instance around the specified target object using the given invocation handler.
     *
     * @param target  the object to be proxied
     * @param handler the invocation handler to dispatch method invocations to
     * @return a new proxy object that wraps the specified target object
     */
    <T> T createProxy(T target, InvocationHandler handler);

}
