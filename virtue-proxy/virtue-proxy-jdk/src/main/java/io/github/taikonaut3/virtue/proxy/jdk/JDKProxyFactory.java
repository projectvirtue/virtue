package io.github.taikonaut3.virtue.proxy.jdk;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.proxy.AbstractProxyFactory;
import io.github.taikonaut3.virtue.proxy.InvocationHandler;

import java.lang.reflect.Proxy;

import static io.github.taikonaut3.virtue.common.constant.Components.ProxyFactory.JDK;

/**
 * Create Proxy By JDK
 */
@ServiceProvider(JDK)
public class JDKProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doCreateProxy(Class<T> interfaceClass, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                new JDKInvocationHandler(interfaceClass, handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doCreateProxy(T target, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new JDKInvocationHandler(target, handler));
    }

}
