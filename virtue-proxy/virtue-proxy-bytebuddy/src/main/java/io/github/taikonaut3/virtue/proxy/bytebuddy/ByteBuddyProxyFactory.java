package io.github.taikonaut3.virtue.proxy.bytebuddy;

import io.github.taikonaut3.virtue.common.spi.ServiceProvider;
import io.github.taikonaut3.virtue.proxy.AbstractProxyFactory;
import io.github.taikonaut3.virtue.proxy.InvocationHandler;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

import static io.github.taikonaut3.virtue.common.constant.Components.ProxyFactory.BYTEBUDDY;

/**
 * Create Proxy  performance is not good
 */
@ServiceProvider(BYTEBUDDY)
@SuppressWarnings("unchecked")
public class ByteBuddyProxyFactory extends AbstractProxyFactory {

    @Override
    protected <T> T doCreateProxy(Class<T> interfaceClass, InvocationHandler handler) {
        try {
            return new ByteBuddy()
                    .subclass(interfaceClass)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new MethodInterceptor(interfaceClass, handler).new InterfaceInterceptor()))
                    .make()
                    .load(interfaceClass.getClassLoader())
                    .getLoaded().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected <T> T doCreateProxy(T target, InvocationHandler handler) {
        try {
            return (T) new ByteBuddy()
                    .subclass(target.getClass())
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new MethodInterceptor(target, handler).new InstanceInterceptor()))
                    .make()
                    .load(target.getClass().getClassLoader())
                    .getLoaded().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
